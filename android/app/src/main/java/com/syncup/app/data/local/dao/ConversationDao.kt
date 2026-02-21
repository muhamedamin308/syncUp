package com.syncup.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syncup.app.data.local.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@Dao
interface ConversationDao {

    // insert or update conversation
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertConversation(conversation: ConversationEntity)

    // observe all conversations
    @Query("SELECT * FROM conversations ORDER BY updatedAt DESC")
    fun observeConversations(): Flow<List<ConversationEntity>>

    // get conversation by id
    @Query("SELECT * FROM conversations WHERE id = :conversationId")
    suspend fun getConversationById(conversationId: String): ConversationEntity?

    // here we are updating the last message id and updated at
    @Query(
        """
        UPDATE conversations 
        SET lastMessageId = :lastMessageId, updatedAt = :updatedAt 
        WHERE id = :conversationId
    """
    )
    suspend fun updatePreview(conversationId: String, lastMessageId: String, updatedAt: Long)

    // here we are updating the unread count
    @Query("UPDATE conversations SET unreadCount = :count WHERE id = :conversationId")
    suspend fun updateUnreadCount(conversationId: String, count: Int)

    // here we are resetting the unread count
    @Query("UPDATE conversations SET unreadCount = 0 WHERE id = :conversationId")
    suspend fun resetUnreadCount(conversationId: String)
}