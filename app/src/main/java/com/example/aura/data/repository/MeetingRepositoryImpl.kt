package com.example.aura.data.repository

import com.example.aura.data.local.dao.MeetingDao
import com.example.aura.data.local.entity.ActionItemEntity
import com.example.aura.data.local.entity.DecisionEntity
import com.example.aura.data.local.entity.MeetingEntity
import com.example.aura.data.local.entity.MessageDraftEntity
import com.example.aura.domain.model.*
import com.example.aura.domain.repository.MeetingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val dao: MeetingDao
) : MeetingRepository {

    override fun getAllMeetings(): Flow<List<Meeting>> =
        dao.getAllMeetings().map { it.map { entity -> entity.toDomain() } }

    override fun getMeetingById(id: UUID): Flow<Meeting?> =
        dao.getMeetingById(id).map { it?.toDomain() }

    override fun getActionItemsForMeeting(meetingId: UUID): Flow<List<ActionItem>> =
        dao.getActionItemsForMeeting(meetingId).map { it.map { entity -> entity.toDomain() } }

    override fun getAllActionItems(): Flow<List<ActionItem>> =
        dao.getAllActionItems().map { it.map { entity -> entity.toDomain() } }

    override fun getDecisionsForMeeting(meetingId: UUID): Flow<List<Decision>> =
        dao.getDecisionsForMeeting(meetingId).map { it.map { entity -> entity.toDomain() } }

    override fun getMessageDraftsForMeeting(meetingId: UUID): Flow<List<MessageDraft>> =
        dao.getMessageDraftsForMeeting(meetingId).map { it.map { entity -> entity.toDomain() } }

    override suspend fun insertMeeting(meeting: Meeting) =
        dao.insertMeeting(meeting.toEntity())

    override suspend fun updateMeeting(meeting: Meeting) =
        dao.updateMeeting(meeting.toEntity())

    override suspend fun insertActionItems(items: List<ActionItem>) =
        dao.insertActionItems(items.map { it.toEntity() })

    override suspend fun updateActionItem(item: ActionItem) =
        dao.updateActionItem(item.toEntity())

    override suspend fun insertDecisions(decisions: List<Decision>) =
        dao.insertDecisions(decisions.map { it.toEntity() })

    override suspend fun insertMessageDrafts(drafts: List<MessageDraft>) =
        dao.insertMessageDrafts(drafts.map { it.toEntity() })

    override suspend fun updateMessageDraft(draft: MessageDraft) =
        dao.updateMessageDraft(draft.toEntity())
}

// Mappers
fun MeetingEntity.toDomain() = Meeting(
    id = id,
    title = title,
    recordedAt = recordedAt,
    durationSeconds = durationSeconds,
    audioFilePath = audioFilePath,
    transcript = transcript,
    summary = summary,
    sentiment = Sentiment.valueOf(sentiment),
    pipelineStatus = PipelineStatus.valueOf(pipelineStatus),
    aiMode = AiMode.valueOf(aiMode)
)

fun Meeting.toEntity() = MeetingEntity(
    id = id,
    title = title,
    recordedAt = recordedAt,
    durationSeconds = durationSeconds,
    audioFilePath = audioFilePath,
    transcript = transcript,
    summary = summary,
    sentiment = sentiment.name,
    pipelineStatus = pipelineStatus.name,
    aiMode = aiMode.name
)

fun ActionItemEntity.toDomain() = ActionItem(
    id = id,
    meetingId = meetingId,
    task = task,
    owner = owner,
    deadline = deadline,
    priority = Priority.valueOf(priority),
    confidence = confidence,
    isComplete = isComplete,
    snoozedUntil = snoozedUntil,
    dispatchedAt = dispatchedAt,
    dispatchChannel = dispatchChannel
)

fun ActionItem.toEntity() = ActionItemEntity(
    id = id,
    meetingId = meetingId,
    task = task,
    owner = owner,
    deadline = deadline,
    priority = priority.name,
    confidence = confidence,
    isComplete = isComplete,
    snoozedUntil = snoozedUntil,
    dispatchedAt = dispatchedAt,
    dispatchChannel = dispatchChannel
)

fun DecisionEntity.toDomain() = Decision(id, meetingId, decisionText)
fun Decision.toEntity() = DecisionEntity(id, meetingId, decisionText)

fun MessageDraftEntity.toDomain() = MessageDraft(
    id = id,
    meetingId = meetingId,
    owner = owner,
    channel = channel,
    subject = subject,
    body = body,
    sentAt = sentAt,
    status = DraftStatus.valueOf(status)
)

fun MessageDraft.toEntity() = MessageDraftEntity(
    id = id,
    meetingId = meetingId,
    owner = owner,
    channel = channel,
    subject = subject,
    body = body,
    sentAt = sentAt,
    status = status.name
)
