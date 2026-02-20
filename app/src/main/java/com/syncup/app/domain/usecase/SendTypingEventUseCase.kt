package com.syncup.app.domain.usecase

import com.syncup.app.domain.model.TypingEvent
import com.syncup.app.domain.repository.SocketRepository
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SendTypingEventUseCase @Inject constructor(
    private val socketRepository: SocketRepository
) {
    suspend operator fun invoke(event: TypingEvent) =
        socketRepository.sendTypingEvent(event)
}