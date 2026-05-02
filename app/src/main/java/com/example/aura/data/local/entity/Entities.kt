package com.example.aura.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "meetings")
data class MeetingEntity(
    @PrimaryKey val id: UUID,
    val title: String,
    val recordedAt: Long,
    val durationSeconds: Int?,
    val audioFilePath: String?,
    val transcript: String,
    val summary: String?,
    val sentiment: String,
    val pipelineStatus: String,
    val aiMode: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "action_items")
data class ActionItemEntity(
    @PrimaryKey val id: UUID,
    val meetingId: UUID,
    val task: String,
    val owner: String?,
    val deadline: Long?,
    val priority: String,
    val confidence: Float,
    val isComplete: Boolean,
    val snoozedUntil: Long?,
    val dispatchedAt: Long?,
    val dispatchChannel: String?
)

@Entity(tableName = "decisions")
data class DecisionEntity(
    @PrimaryKey val id: UUID,
    val meetingId: UUID,
    val decisionText: String
)

@Entity(tableName = "message_drafts")
data class MessageDraftEntity(
    @PrimaryKey val id: UUID,
    val meetingId: UUID,
    val owner: String,
    val channel: String,
    val subject: String?,
    val body: String,
    val sentAt: Long?,
    val status: String
)
