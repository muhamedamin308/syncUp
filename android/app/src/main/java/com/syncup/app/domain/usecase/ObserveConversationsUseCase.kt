package com.syncup.app.domain.usecase

import com.syncup.app.domain.model.Conversation
import com.syncup.app.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class ObserveConversationsUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository
) {
    operator fun invoke(): Flow<List<Conversation>> =
        conversationRepository.observeConversations()
}