package com.syncup.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val avatarUrl: String?,
    val isOnline: Boolean
)
