package com.syncup.app.domain.model

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
data class User(
    val id: String,
    val username: String,
    val avatarUrl: String?,
    val isOnline: Boolean
)
