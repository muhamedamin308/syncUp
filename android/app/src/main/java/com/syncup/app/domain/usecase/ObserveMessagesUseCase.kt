package com.syncup.app.domain.usecase

import com.syncup.app.domain.repository.MessageRepository
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class ObserveMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(conversationId: String) =
        messageRepository.observeMessages(conversationId)
}