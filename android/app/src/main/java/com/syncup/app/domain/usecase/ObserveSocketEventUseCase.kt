package com.syncup.app.domain.usecase

import com.syncup.app.domain.model.SocketEvent
import com.syncup.app.domain.repository.SocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

class ObserveSocketEventsUseCase @Inject constructor(
    private val socketRepository: SocketRepository,
) {
    operator fun invoke(): Flow<SocketEvent> = socketRepository.events
}