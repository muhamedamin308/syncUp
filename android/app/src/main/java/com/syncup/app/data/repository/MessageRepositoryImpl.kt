package com.syncup.app.data.repository

import com.syncup.app.data.local.dao.MessageDao
import com.syncup.app.data.local.mapper.toDomain
import com.syncup.app.data.local.mapper.toEntity
import com.syncup.app.data.remote.WebSocketClient
import com.syncup.app.data.remote.dto.SendMessageDto
import com.syncup.app.domain.model.Message
import com.syncup.app.domain.model.MessageStatus
import com.syncup.app.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val webSocketClient: WebSocketClient,
) : MessageRepository {

    override fun observeMessages(conversationId: String): Flow<List<Message>> =
        messageDao.observeMessages(conversationId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun sendMessage(message: Message) {
        // 1. Optimistic insert into local DB with SENDING status
        messageDao.upsertMessage(message.toEntity())

        // 2. Fire-and-forget over WebSocket - Server will ACK back
        webSocketClient.send(
            SendMessageDto(
                localId = message.id,
                conversationId = message.conversationId,
                senderId = message.senderId,
                receiverId = message.receiverId,
                text = message.text,
                timestamp = message.timestamp
            )
        )
    }

    override suspend fun updateMessageStatus(
        messageId: String,
        status: MessageStatus,
    ) {
        messageDao.updateStatus(messageId, status.name)
    }

    override suspend fun getMessages(conversationId: String): List<Message> =
        messageDao.getMessages(conversationId).map { it.toDomain() }
}