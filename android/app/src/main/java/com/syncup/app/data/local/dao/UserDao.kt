package com.syncup.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syncup.app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

@Dao
interface UserDao {
    // Insert or update a user in the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertUser(user: UserEntity)

    // get a user by its id
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    // update the presence of a user
    @Query("UPDATE users SET isOnline = :isOnline WHERE id = :userId")
    suspend fun updatePresence(userId: String, isOnline: Boolean)

    // observe the presence of a user
    @Query("SELECT isOnline FROM users WHERE id = :userId")
    fun observePresence(userId: String): Flow<Boolean>
}