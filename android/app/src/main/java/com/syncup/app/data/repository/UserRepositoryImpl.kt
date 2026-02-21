package com.syncup.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.syncup.app.data.local.dao.UserDao
import com.syncup.app.data.local.mapper.toDomain
import com.syncup.app.data.remote.WebSocketClient
import com.syncup.app.data.remote.dto.PresenceDto
import com.syncup.app.domain.model.User
import com.syncup.app.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val webSocketClient: WebSocketClient,
    private val dataStore: DataStore<Preferences>,
) : UserRepository {

    companion object {
        val KEY_CURRENT_USER_ID = stringPreferencesKey("current_user_id")

        val DEFAULT_USER = User(
            id = "user_001",
            username = "guest_username",
            avatarUrl = null,
            isOnline = true
        )
    }

    override suspend fun getCurrentUser(): User {
        val userId = dataStore.data.first()[KEY_CURRENT_USER_ID]
            ?: DEFAULT_USER.id
        return userDao.getUserById(userId)?.toDomain() ?: DEFAULT_USER
    }

    override suspend fun setPresence(isOnline: Boolean) {
        val user = getCurrentUser()
        userDao.updatePresence(user.id, isOnline)
        webSocketClient.send(
            PresenceDto(userId = user.id, isOnline = isOnline)
        )
    }

    override fun observeUserPresence(userId: String): Flow<Boolean> =
        userDao.observePresence(userId)

    override suspend fun getUser(userId: String): User? =
        userDao.getUserById(userId)?.toDomain()
}