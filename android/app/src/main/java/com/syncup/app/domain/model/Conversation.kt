package com.syncup.app.domain.model

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class Conversation(
    val id: String,
    val otherUser: User,
    val lastMessage: Message?,
    val unreadCount: Int,
    val updatedAt: Long // Used for sorting the list (most recent on top)
)
