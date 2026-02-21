package com.syncup.app.data.remote

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.syncup.app.data.remote.dto.AckDto
import com.syncup.app.data.remote.dto.IncomingMessageDto
import com.syncup.app.data.remote.dto.PresenceDto
import com.syncup.app.data.remote.dto.StatusUpdateDto
import com.syncup.app.data.remote.dto.TypingDto
import com.syncup.app.domain.model.Message
import com.syncup.app.domain.model.MessageStatus
import com.syncup.app.domain.model.SocketEvent
import com.syncup.app.domain.model.TypingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@Singleton
class WebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val baseUrl: String,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _events = MutableSharedFlow<SocketEvent>(
        replay = 0,
        extraBufferCapacity = 64
    )
    val events = _events.asSharedFlow()

    private var webSocket: WebSocket? = null

    fun connect(userId: String) {
        val request = Request.Builder()
            .url("$baseUrl?userId=$userId")
            .build()

        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                emit(SocketEvent.Connected)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                parseAndEmit(text)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                emit(SocketEvent.Disconnected)
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                emit(SocketEvent.Error(t))
                // Simple reconnect attempt after a failure
                emit(SocketEvent.Disconnected)
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, null)
        webSocket = null
    }

    fun send(payload: Any) {
        val json = gson.toJson(payload)
        webSocket?.send(json)
    }

    private fun parseAndEmit(text: String) {
        try {
            val json = JsonParser.parseString(text).asJsonObject
            val type = json.get("type")?.toString() ?: return

            val event: SocketEvent? = when (type) {
                "INCOMING_MESSAGE" -> {
                    val dto = gson.fromJson(json, IncomingMessageDto::class.java)
                    SocketEvent.IncomingMessage(dto.toDomain())
                }

                "MESSAGE_ACK" -> {
                    val dto = gson.fromJson(json, AckDto::class.java)
                    SocketEvent.MessageAcknowledged(
                        localId = dto.localId,
                        confirmedId = dto.confirmedId,
                        status = MessageStatus.valueOf(dto.status)
                    )
                }

                "STATUS_UPDATE" -> {
                    val dto = gson.fromJson(json, StatusUpdateDto::class.java)
                    SocketEvent.StatusUpdate(
                        messageId = dto.messageId,
                        newStatus = MessageStatus.valueOf(dto.status)
                    )
                }

                "TYPING" -> {
                    val dto = gson.fromJson(json, TypingDto::class.java)
                    SocketEvent.TypingIndicator(
                        TypingEvent(
                            conversationId = dto.conversationId,
                            senderId = dto.senderId,
                            isTyping = dto.isTyping
                        )
                    )
                }

                "PRESENCE" -> {
                    val dto = gson.fromJson(json, PresenceDto::class.java)
                    SocketEvent.PresenceChanged(
                        userId = dto.userId,
                        isOnline = dto.isOnline
                    )
                }

                else -> null
            }

            event?.let { emit(it) }

        } catch (e: Exception) {
            emit(SocketEvent.Error(e))
        }
    }

    private fun emit(event: SocketEvent) {
        scope.launch { _events.emit(event) }
    }
}

// ─── DTO → Domain mapper (scoped here since it's remote-layer only) ───────────
private fun IncomingMessageDto.toDomain() = Message(
    id = id,
    conversationId = conversationId,
    senderId = senderId,
    receiverId = receiverId,
    text = text,
    timestamp = timestamp,
    status = MessageStatus.valueOf(status)
)