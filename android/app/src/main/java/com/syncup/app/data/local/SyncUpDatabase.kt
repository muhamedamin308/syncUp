package com.syncup.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.syncup.app.data.local.dao.ConversationDao
import com.syncup.app.data.local.dao.MessageDao
import com.syncup.app.data.local.dao.UserDao
import com.syncup.app.data.local.entity.ConversationEntity
import com.syncup.app.data.local.entity.MessageEntity
import com.syncup.app.data.local.entity.UserEntity

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Database(
    entities = [
        UserEntity::class,
        MessageEntity::class,
        ConversationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SyncUpDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
}