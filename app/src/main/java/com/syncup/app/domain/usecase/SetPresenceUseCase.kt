package com.syncup.app.domain.usecase

import com.syncup.app.domain.repository.UserRepository
import javax.inject.Inject

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
class SetPresenceUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isOnline: Boolean) =
        userRepository.setPresence(isOnline)
}