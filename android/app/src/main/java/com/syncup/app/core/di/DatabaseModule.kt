package com.syncup.app.core.di

import android.content.Context
import androidx.room.Room
import com.syncup.app.data.local.SyncUpDatabase
import com.syncup.app.data.local.dao.ConversationDao
import com.syncup.app.data.local.dao.MessageDao
import com.syncup.app.data.local.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SyncUpDatabase =
        Room.databaseBuilder(
            context,
            SyncUpDatabase::class.java,
            "syncup.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideUserDao(db: SyncUpDatabase): UserDao = db.userDao()
    @Provides
    fun provideMessageDao(db: SyncUpDatabase): MessageDao = db.messageDao()
    @Provides
    fun provideConversationDao(db: SyncUpDatabase): ConversationDao = db.conversationDao()
}