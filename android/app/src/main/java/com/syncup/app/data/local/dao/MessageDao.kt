package com.syncup.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syncup.app.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@Dao
interface MessageDao {

    // Insert new message to messages table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMessage(message: MessageEntity)

    // Observe all messages in a conversation
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun observeMessages(conversationId: String): Flow<List<MessageEntity>>

    // Get all messages in a conversation
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    suspend fun getMessages(conversationId: String): List<MessageEntity>

    // Update message status
    @Query("UPDATE messages SET status = :status WHERE id = :messageId")
    suspend fun updateStatus(messageId: String, status: String)

    // When server acknowledges an optimistic message, we swap the temp ID for the real one
    @Query("UPDATE messages SET id = :confirmedId, status = :status WHERE id = :localId")
    suspend fun confirmMessage(localId: String, confirmedId: String, status: String)

    // Mark all messages in a conversation as read
    @Query("UPDATE messages SET status = 'READ' WHERE conversationId = :conversationId AND senderId != :currentUserId")
    suspend fun markAllAsRead(conversationId: String, currentUserId: String)

    // get the total count of unread messages in a conversation
    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId AND status != 'READ' AND senderId != :currentUserId")
    suspend fun getUnreadCount(conversationId: String, currentUserId: String): Int

    // Get a message by its ID
    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: String): MessageEntity?
}