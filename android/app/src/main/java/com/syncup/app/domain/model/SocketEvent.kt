package com.syncup.app.domain.model


/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

sealed class SocketEvent {

    data class IncomingMessage(val message: Message) : SocketEvent()

    data class MessageAcknowledged(
        val localId: String,
        val confirmedId: String,
        val status: MessageStatus,
    ) : SocketEvent()

    data class StatusUpdate(
        val messageId: String,
        val newStatus: MessageStatus,
    ) : SocketEvent()

    data class TypingIndicator(val event: TypingEvent) : SocketEvent()

    data class PresenceChanged(
        val userId: String,
        val isOnline: Boolean,
    ) : SocketEvent()

    object Connected : SocketEvent()
    object Disconnected : SocketEvent()
    data class Error(val throwable: Throwable) : SocketEvent()
}