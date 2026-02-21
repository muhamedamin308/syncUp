package com.syncup.app.core.di

import com.syncup.app.data.repository.ConversationRepositoryImpl
import com.syncup.app.data.repository.MessageRepositoryImpl
import com.syncup.app.data.repository.SocketRepositoryImpl
import com.syncup.app.data.repository.UserRepositoryImpl
import com.syncup.app.domain.repository.ConversationRepository
import com.syncup.app.domain.repository.MessageRepository
import com.syncup.app.domain.repository.SocketRepository
import com.syncup.app.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author Muhamed Amin Hassan on 21,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSocketRepository(impl: SocketRepositoryImpl): SocketRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Binds
    @Singleton
    abstract fun bindConversationRepository(impl: ConversationRepositoryImpl): ConversationRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}