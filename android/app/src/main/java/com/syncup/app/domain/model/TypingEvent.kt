package com.syncup.app.domain.model

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class TypingEvent(
    val conversationId: String,
    val senderId: String,
    val isTyping: Boolean
)
