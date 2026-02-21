package com.syncup.app.domain.repository

import com.syncup.app.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
interface UserRepository {
    // get the currently logged-in user (stored locally)
    suspend fun getCurrentUser(): User

    // update the current user's online/offline presence
    suspend fun setPresence(isOnline: Boolean)

    // Observe another user's presence in real time
    fun observeUserPresence(userId: String): Flow<Boolean>

    // Fetch a specific user's profile (from cache or server)
    suspend fun getUser(userId: String): User?
}