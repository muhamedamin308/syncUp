package com.syncup.app.domain.repository

import com.syncup.app.domain.model.SocketEvent
import com.syncup.app.domain.model.TypingEvent
import kotlinx.coroutines.flow.Flow

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface SocketRepository {
    // A single shared stream of all incoming WebSocket events
    val events: Flow<SocketEvent>

    // Connect the WebSocket for a given user session
    fun connect(userId: String)

    // Gracefully disconnect
    fun disconnect()

    // Send a raw JSON payload (used internally by other repositories)
    suspend fun sendTypingEvent(event: TypingEvent)
}