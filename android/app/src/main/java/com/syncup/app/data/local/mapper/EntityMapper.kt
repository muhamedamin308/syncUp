package com.syncup.app.data.local.mapper

import com.syncup.app.data.local.entity.MessageEntity
import com.syncup.app.data.local.entity.UserEntity
import com.syncup.app.domain.model.Message
import com.syncup.app.domain.model.MessageStatus
import com.syncup.app.domain.model.User

/**
 * @author Muhamed Amin Hassan on 20,February,2026
 * @see <a href="https://github.com/muhamedamin308">Muhamed's Github</a>,
 * Egypt, Cairo.
 */

// ─── User ────────────────────────────────────────────────────────────────────

fun UserEntity.toDomain() = User(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    isOnline = isOnline
)

fun User.toEntity() = UserEntity(
    id = id,
    username = username,
    avatarUrl = avatarUrl,
    isOnline = isOnline
)

// ─── Message ─────────────────────────────────────────────────────────────────

fun MessageEntity.toDomain() = Message(
    id = id,
    conversationId = conversationId,
    senderId = senderId,
    receiverId = receiverId,
    text = text,
    timestamp = timestamp,
    status = MessageStatus.valueOf(status)
)

fun Message.toEntity() = MessageEntity(
    id = id,
    conversationId = conversationId,
    senderId = senderId,
    receiverId = receiverId,
    text = text,
    timestamp = timestamp,
    status = status.name
)