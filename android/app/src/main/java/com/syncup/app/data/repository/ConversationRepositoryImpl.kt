package com.syncup.app.data.repository

import com.syncup.app.data.local.dao.ConversationDao
import com.syncup.app.data.local.dao.MessageDao
import com.syncup.app.data.local.dao.UserDao
import com.syncup.app.data.local.mapper.toDomain
import com.syncup.app.data.remote.WebSocketClient
import com.syncup.app.domain.model.Conversation
import com.syncup.app.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class ConversationRepositoryImpl @Inject constructor(
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
    private val userDao: UserDao,
    private val webSocketClient: WebSocketClient,
) : ConversationRepository {

    override fun observeConversations(): Flow<List<Conversation>> =
        conversationDao.observeConversations().map { entities ->
            entities.mapNotNull { entity ->
                val otherUser = userDao.getUserById(entity.otherUserId)?.toDomain()
                    ?: return@mapNotNull null

                val lastMessage = entity.lastMessageId?.let {
                    messageDao.getMessageById(it)?.toDomain()
                }

                Conversation(
                    id = entity.id,
                    otherUser = otherUser,
                    lastMessage = lastMessage,
                    unreadCount = entity.unreadCount,
                    updatedAt = entity.updatedAt
                )
            }
        }

    override suspend fun getConversation(conversationId: String): Conversation? {
        val entity = conversationDao.getConversationById(conversationId) ?: return null
        val otherUser = userDao.getUserById(entity.otherUserId)?.toDomain() ?: return null
        val lastMessage = entity.lastMessageId?.let {
            messageDao.getMessageById(it)?.toDomain()
        }

        return Conversation(
            id = entity.id,
            otherUser = otherUser,
            lastMessage = lastMessage,
            unreadCount = entity.unreadCount,
            updatedAt = entity.updatedAt
        )
    }

    override suspend fun markConversationAsRead(conversationId: String) {
        // Reset unread badge locally
        conversationDao.resetUnreadCount(conversationId)
    }

    override suspend fun updateConversationPreview(
        conversationId: String,
        lastMessageId: String,
    ) {
        conversationDao.updatePreview(
            conversationId = conversationId,
            lastMessageId = lastMessageId,
            updatedAt = System.currentTimeMillis()
        )
    }
}