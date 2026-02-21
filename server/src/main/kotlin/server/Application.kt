package server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.concurrent.ConcurrentHashMap

val sessions = ConcurrentHashMap<String, DefaultWebSocketSession>()

fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0"
    ) {
        install(WebSockets)
        routing {
            webSocket("/chat") {
                val userId = call.request.queryParameters["userId"]
                    ?: return@webSocket close(
                        CloseReason(CloseReason.Codes.VIOLATED_POLICY, message = "Missing userId")
                    )

                // Register session
                sessions[userId] = this
                println("✅ \$userId connected. Active: \${sessions.keys}")

                // Broadcast presence: online
                broadcast(
                    from = userId,
                    message = """{"type":"PRESENCE","userId":"$userId","isOnline":true}"""
                )

                try {
                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text)
                            handleFrame(userId, frame.readText())
                    }
                } finally {
                    // Session Ended - broadcast presence: offline
                    sessions.remove(userId)
                    println("❌ $userId disconnected.")
                    broadcastAll("""{"type":"PRESENCE","userId":"$userId","isOnline":false}""")
                }
            }
        }
    }.start()
}

// ─── Frame handler ────────────────────────────────────────────────────────────

suspend fun handleFrame(senderId: String, text: String) {
    println("📨 [$senderId] $text")
    val json = Json.parseToJsonElement(text).jsonObject
    val type = json["type"]?.jsonPrimitive?.content ?: return

    when (type) {
        "SEND_MESSAGE" -> {
            val localId = json["localId"]!!.jsonPrimitive.content
            val receiverId = json["receiverId"]!!.jsonPrimitive.content
            val confirmedId = "msg_${System.currentTimeMillis()}"

            // 1. ACK back to sender
            val ack = """
                {
                  "type": "MESSAGE_ACK",
                  "localId": "$localId",
                  "confirmedId": "$confirmedId",
                  "status": "SENT"
                }
            """.trimIndent()
            sessions[senderId]?.send(Frame.Text(ack))

            // 2. Forward message to receiver (if online)
            val forward = """
                {
                  "type": "INCOMING_MESSAGE",
                  "id": "$confirmedId",
                  "conversationId": ${json["conversationId"]},
                  "senderId": "$senderId",
                  "receiverId": "$receiverId",
                  "text": ${json["text"]},
                  "timestamp": ${json["timestamp"]},
                  "status": "DELIVERED"
                }
            """.trimIndent()
            sessions[receiverId]?.send(Frame.Text(forward))

            // 3. If receiver is online, send "DELIVERED" status back to sender
            if (sessions.containsKey(receiverId)) {
                val delivered = """
                    {
                      "type": "STATUS_UPDATE",
                      "messageId": "$confirmedId",
                      "status": "DELIVERED"
                    }
                """.trimIndent()
                sessions[senderId]?.send(Frame.Text(delivered))
            }
        }

        "TYPING" -> {
            // Forward typing event to the other user
            val receiverId = json["conversationId"]!!.jsonPrimitive.content
                .replace("conv_", "")
                .split("_")
                .first { it != senderId }
            sessions[receiverId]?.send(Frame.Text(text))
        }

        "PRESENCE" -> {
            broadcastAll(text)
        }

        "READ_RECEIPT" -> {
            val conversationId = json["conversationId"]!!.jsonPrimitive.content
            val otherId = conversationId.replace("conv_", "").split("_").first { it != senderId }
            // Notify the original sender that their message was read
            sessions[otherId]?.send(
                Frame.Text(
                    """{"type":"STATUS_UPDATE","messageId":"all","status":"READ"}"""
                )
            )
        }
    }
}

// ─── Broadcast helpers ────────────────────────────────────────────────────────
suspend fun broadcastAll(message: String) {
    sessions.values.forEach { it.send(Frame.Text(message)) }
}

suspend fun broadcast(from: String, message: String) {
    sessions.filterKeys { it != from }.values.forEach { it.send(Frame.Text(message)) }
}