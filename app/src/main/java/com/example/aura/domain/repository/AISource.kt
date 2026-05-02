package com.example.aura.domain.repository

import kotlinx.coroutines.flow.Flow

data class ExtractionResult(
    val summary: String,
    val decisions: List<String>,
    val actionItems: List<ExtractedActionItem>,
    val participants: List<String>,
    val sentiment: String,
    val followUpNeeded: Boolean
)

data class ExtractedActionItem(
    val task: String,
    val owner: String?,
    val deadline: String?,
    val priority: String,
    val confidence: Float
)

data class ComposedMessage(
    val subject: String?,
    val body: String
)

interface AISource {
    suspend fun isAvailable(): Boolean
    suspend fun extractMeetingInfo(transcript: String, dateHint: String, titleHint: String): Flow<ExtractionResult>
    suspend fun composeFollowUp(ownerName: String, actionItems: List<String>, sentiment: String, meetingTitle: String, myName: String, channel: String): ComposedMessage
}
