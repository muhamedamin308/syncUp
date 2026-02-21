package com.syncup.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Long,
    val status: String   // Stored as string e.g. "SENDING", "SENT", "DELIVERED", "READ"
)