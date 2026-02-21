package com.syncup.app.domain.usecase

import com.syncup.app.domain.repository.ConversationRepository
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class MarkConversationsReadUseCase @Inject constructor(
    private val conversationRepository: ConversationRepository,
) {
    suspend operator fun invoke(conversationId: String) =
        conversationRepository.markConversationAsRead(conversationId)
}