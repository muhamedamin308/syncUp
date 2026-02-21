package com.syncup.app.domain.usecase

import com.syncup.app.domain.model.Message
import com.syncup.app.domain.repository.MessageRepository
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class SendMessageUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(message: Message) =
        messageRepository.sendMessage(message)
}