package com.syncup.app.domain.repository

import com.syncup.app.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface ConversationRepository {
    // Observe the full sorted conversation list as a reactive stream
    fun observeConversations(): Flow<List<Conversation>>

    // Get a single conversation by ID
    suspend fun getConversation(conversationId: String): Conversation?

    // Mark all messages in a conversation as read (triggers status update events)
    suspend fun markConversationAsRead(conversationId: String)

    // Called when a new message arrives to update the conversation's preview and order
    suspend fun updateConversationPreview(conversationId: String, lastMessageId: String)
}