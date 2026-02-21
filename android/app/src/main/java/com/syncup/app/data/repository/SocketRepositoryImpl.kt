package com.syncup.app.data.repository

import com.syncup.app.data.remote.WebSocketClient
import com.syncup.app.data.remote.dto.TypingDto
import com.syncup.app.domain.model.SocketEvent
import com.syncup.app.domain.model.TypingEvent
import com.syncup.app.domain.repository.SocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SocketRepositoryImpl @Inject constructor(
    private val webSocketClient: WebSocketClient,
) : SocketRepository {
    override val events: Flow<SocketEvent>
        get() = webSocketClient.events

    override fun connect(userId: String) {
        webSocketClient.connect(userId)
    }

    override fun disconnect() {
        webSocketClient.disconnect()
    }

    override suspend fun sendTypingEvent(event: TypingEvent) {
        webSocketClient.send(
            TypingDto(
                conversationId = event.conversationId,
                senderId = event.senderId,
                isTyping = event.isTyping,
            )
        )
    }
}