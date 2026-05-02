package com.example.aura.domain.model

import java.util.UUID

data class Meeting(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val recordedAt: Long = System.currentTimeMillis(),
    val durationSeconds: Int? = null,
    val audioFilePath: String? = null,
    val transcript: String,
    val summary: String? = null,
    val sentiment: Sentiment = Sentiment.UNCLEAR,
    val pipelineStatus: PipelineStatus = PipelineStatus.PROCESSING,
    val aiMode: AiMode = AiMode.ON_DEVICE
)

enum class Sentiment {
    PRODUCTIVE, TENSE, UNCLEAR
}

enum class PipelineStatus {
    PROCESSING, COMPLETE, FAILED, DISPATCHED
}

enum class AiMode {
    ON_DEVICE, CLOUD, MANUAL
}

data class ActionItem(
    val id: UUID = UUID.randomUUID(),
    val meetingId: UUID,
    val task: String,
    val owner: String? = null,
    val deadline: Long? = null,
    val priority: Priority = Priority.MEDIUM,
    val confidence: Float = 1.0f,
    val isComplete: Boolean = false,
    val snoozedUntil: Long? = null,
    val dispatchedAt: Long? = null,
    val dispatchChannel: String? = null
)

enum class Priority {
    HIGH, MEDIUM, LOW
}

data class Decision(
    val id: UUID = UUID.randomUUID(),
    val meetingId: UUID,
    val decisionText: String
)

data class MessageDraft(
    val id: UUID = UUID.randomUUID(),
    val meetingId: UUID,
    val owner: String,
    val channel: String,
    val subject: String? = null,
    val body: String,
    val sentAt: Long? = null,
    val status: DraftStatus = DraftStatus.DRAFT
)

enum class DraftStatus {
    DRAFT, SENT, DISMISSED
}
