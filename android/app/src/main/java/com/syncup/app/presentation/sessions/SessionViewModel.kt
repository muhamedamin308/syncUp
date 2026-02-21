package com.syncup.app.presentation.sessions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syncup.app.data.local.dao.ConversationDao
import com.syncup.app.data.local.dao.MessageDao
import com.syncup.app.data.local.dao.UserDao
import com.syncup.app.data.local.entity.ConversationEntity
import com.syncup.app.data.local.entity.UserEntity
import com.syncup.app.data.local.mapper.toEntity
import com.syncup.app.domain.model.SocketEvent
import com.syncup.app.domain.repository.ConversationRepository
import com.syncup.app.domain.repository.MessageRepository
import com.syncup.app.domain.repository.SocketRepository
import com.syncup.app.domain.repository.UserRepository
import com.syncup.app.domain.usecase.ObserveSocketEventsUseCase
import com.syncup.app.domain.usecase.SetPresenceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val socketRepository: SocketRepository,
    private val messageRepository: MessageRepository,
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository,
    private val observeSocketEvents: ObserveSocketEventsUseCase,
    private val setPresence: SetPresenceUseCase,
    private val userDao: UserDao,
    private val conversationDao: ConversationDao,
    private val messageDao: MessageDao,
) : ViewModel() {
    init {
        viewModelScope.launch {
            seedMockDateIfNeeded()
            val currentUser = userRepository.getCurrentUser()
            socketRepository.connect(currentUser.id)
            observeSocketEvents()
                .onEach { event -> handleEvent(event) }
                .launchIn(viewModelScope)
        }
    }

    // ─── Global event router ──────────────────────────────────────────────────
    private suspend fun handleEvent(event: SocketEvent) {
        when (event) {
            is SocketEvent.IncomingMessage -> {
                val message = event.message
                // 1. Persist the new message locally into Room
                messageDao.upsertMessage(message.toEntity())
                // 2. Update Conversation preview and bump to top
                conversationRepository.updateConversationPreview(
                    conversationId = message.conversationId,
                    lastMessageId = message.id
                )
                // 3. Increment the unread count for the conversation
                val entity = conversationDao.getConversationById(message.conversationId)
                entity?.let {
                    conversationDao.updateUnreadCount(
                        conversationId = it.id,
                        count = it.unreadCount + 1
                    )
                }
            }

            is SocketEvent.MessageAcknowledged -> {
                // Swap temp local ID -> Confirmed server ID, and update status
                messageDao.confirmMessage(
                    localId = event.localId,
                    confirmedId = event.confirmedId,
                    status = event.status.name
                )
            }

            is SocketEvent.StatusUpdate -> {
                messageDao.updateStatus(
                    messageId = event.messageId,
                    status = event.newStatus.name
                )
            }

            is SocketEvent.PresenceChanged -> {
                userDao.updatePresence(event.userId, event.isOnline)
            }

            is SocketEvent.Connected -> {
                // Mark current user as online in local DB
                val user = userRepository.getCurrentUser()
                userDao.updatePresence(user.id, true)
            }

            is SocketEvent.Disconnected -> {
                // Mark current user as offline in local DB
                val user = userRepository.getCurrentUser()
                userDao.updatePresence(user.id, false)
            }

            is SocketEvent.TypingIndicator -> Unit
            is SocketEvent.Error -> Unit
        }
    }

    // ─── Lifecycle-aware presence ─────────────────────────────────────────────
    fun goOnline() {
        viewModelScope.launch { setPresence(true) }
    }

    fun goOffline() {
        viewModelScope.launch { setPresence(false) }
    }

    // ─── Mock data seed ───────────────────────────────────────────────────────
    // Pre-populates DB with two users and one conversation so the app
    // isn't blank on first launch
    private suspend fun seedMockDateIfNeeded() {
        if (userDao.getUserById("user_001") != null) return

        userDao.upsertUser(UserEntity("user_001", "Muhamed", null, isOnline = true))
        userDao.upsertUser(UserEntity("user_002", "Ahmed", null, isOnline = false))

        conversationDao.upsertConversation(
            ConversationEntity(
                id = "conv_user_001_user_002",
                otherUserId = "user_002",
                lastMessageId = null,
                unreadCount = 0,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        socketRepository.disconnect()
    }
}