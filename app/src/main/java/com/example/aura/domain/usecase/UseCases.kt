package com.example.aura.domain.usecase

import com.example.aura.domain.model.*
import com.example.aura.domain.repository.AISource
import com.example.aura.domain.repository.MeetingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class StartRecordingUseCase @Inject constructor() {
    // Placeholder — wired to Android SpeechRecognizer in phase 2
    fun execute(): Flow<String> {
        throw NotImplementedError("Implement using SpeechRecognizer streaming")
    }
}

class RunPipelineUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val aiSource: AISource
) {
    suspend fun execute(meeting: Meeting) {
        // Stage 2: Extraction Agent — collect the last (most complete) emission
        val extraction = aiSource.extractMeetingInfo(
            transcript = meeting.transcript,
            dateHint = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date(meeting.recordedAt)),
            titleHint = meeting.title
        ).first { true }  // collect final emission

        // Persist summary + sentiment back to meeting
        meetingRepository.updateMeeting(
            meeting.copy(
                summary = extraction.summary,
                sentiment = when (extraction.sentiment) {
                    "PRODUCTIVE" -> Sentiment.PRODUCTIVE
                    "TENSE" -> Sentiment.TENSE
                    else -> Sentiment.UNCLEAR
                },
                pipelineStatus = PipelineStatus.COMPLETE
            )
        )

        // Persist decisions
        val decisions = extraction.decisions.map { text ->
            Decision(meetingId = meeting.id, decisionText = text)
        }
        if (decisions.isNotEmpty()) meetingRepository.insertDecisions(decisions)

        // Persist action items
        val actionItems = extraction.actionItems.map { extracted ->
            ActionItem(
                meetingId = meeting.id,
                task = extracted.task,
                owner = extracted.owner,
                priority = when (extracted.priority) {
                    "HIGH" -> Priority.HIGH
                    "LOW" -> Priority.LOW
                    else -> Priority.MEDIUM
                },
                confidence = extracted.confidence
            )
        }
        if (actionItems.isNotEmpty()) meetingRepository.insertActionItems(actionItems)

        // Stage 3: Composition Agent — draft a follow-up per unique owner
        val drafts = actionItems
            .filter { !it.owner.isNullOrBlank() }
            .groupBy { it.owner!! }
            .map { (owner, items) ->
                val composed = aiSource.composeFollowUp(
                    ownerName = owner,
                    actionItems = items.map { it.task },
                    sentiment = extraction.sentiment,
                    meetingTitle = meeting.title,
                    myName = "Gayanthika",
                    channel = "email"
                )
                MessageDraft(
                    meetingId = meeting.id,
                    owner = owner,
                    channel = "email",
                    subject = composed.subject,
                    body = composed.body
                )
            }
        if (drafts.isNotEmpty()) meetingRepository.insertMessageDrafts(drafts)
    }
}

class DispatchMessageUseCase @Inject constructor(
    private val meetingRepository: MeetingRepository
) {
    suspend fun execute(draft: MessageDraft) {
        meetingRepository.updateMessageDraft(
            draft.copy(
                status = DraftStatus.SENT,
                sentAt = System.currentTimeMillis()
            )
        )
    }
}
