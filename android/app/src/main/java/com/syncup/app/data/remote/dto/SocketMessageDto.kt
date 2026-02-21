package com.syncup.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

// Every WebSocket frame has a "type" field so we know how to parse the payload
data class SocketFrameDto(
    @SerializedName("type") val type: String,
    @SerializedName("payload") val payload: Any? = null  // parsed separately per type
)

// ─── Outgoing (Client → Server) ──────────────────────────────────────────────

data class SendMessageDto(
    @SerializedName("type") val type: String = "SEND_MESSAGE",
    @SerializedName("localId") val localId: String,
    @SerializedName("conversationId") val conversationId: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("receiverId") val receiverId: String,
    @SerializedName("text") val text: String,
    @SerializedName("timestamp") val timestamp: Long
)

data class TypingDto(
    @SerializedName("type") val type: String = "TYPING",
    @SerializedName("conversationId") val conversationId: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("isTyping") val isTyping: Boolean
)

data class PresenceDto(
    @SerializedName("type") val type: String = "PRESENCE",
    @SerializedName("userId") val userId: String,
    @SerializedName("isOnline") val isOnline: Boolean
)

data class ReadReceiptDto(
    @SerializedName("type") val type: String = "READ_RECEIPT",
    @SerializedName("conversationId") val conversationId: String,
    @SerializedName("readerId") val readerId: String
)

// ─── Incoming (Server → Client) ──────────────────────────────────────────────

data class IncomingMessageDto(
    @SerializedName("id") val id: String,
    @SerializedName("localId") val localId: String?,    // present on ACK only
    @SerializedName("conversationId") val conversationId: String,
    @SerializedName("senderId") val senderId: String,
    @SerializedName("receiverId") val receiverId: String,
    @SerializedName("text") val text: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("status") val status: String
)

data class StatusUpdateDto(
    @SerializedName("messageId") val messageId: String,
    @SerializedName("status") val status: String
)

data class AckDto(
    @SerializedName("localId") val localId: String,
    @SerializedName("confirmedId") val confirmedId: String,
    @SerializedName("status") val status: String
)