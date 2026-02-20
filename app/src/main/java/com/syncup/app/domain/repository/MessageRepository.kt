package com.syncup.app.domain.repository

import com.syncup.app.domain.model.Message
import com.syncup.app.domain.model.MessageStatus
import kotlinx.coroutines.flow.Flow

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface MessageRepository {
    // observe all messages for a conversation as a reactive stream (from local DB)
    fun observeMessages(conversationId: String): Flow<List<Message>>

    // send a message - optimistic insert happens inside, then WebSocket send
    suspend fun sendMessage(message: Message)

    // update the status of a specific message ( e.g. send -> delivered -> read )
    suspend fun updateMessageStatus(messageId: String, status: MessageStatus)

    // pull messages from local DB for a conversation (one-shot , not reactive)
    suspend fun getMessages(conversationId: String): List<Message>
}