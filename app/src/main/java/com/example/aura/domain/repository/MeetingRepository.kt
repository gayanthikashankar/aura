package com.example.aura.domain.repository

import com.example.aura.domain.model.ActionItem
import com.example.aura.domain.model.Decision
import com.example.aura.domain.model.Meeting
import com.example.aura.domain.model.MessageDraft
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MeetingRepository {
    fun getAllMeetings(): Flow<List<Meeting>>
    fun getMeetingById(id: UUID): Flow<Meeting?>
    fun getActionItemsForMeeting(meetingId: UUID): Flow<List<ActionItem>>
    fun getAllActionItems(): Flow<List<ActionItem>>
    fun getDecisionsForMeeting(meetingId: UUID): Flow<List<Decision>>
    fun getMessageDraftsForMeeting(meetingId: UUID): Flow<List<MessageDraft>>

    suspend fun insertMeeting(meeting: Meeting)
    suspend fun updateMeeting(meeting: Meeting)
    suspend fun insertActionItems(items: List<ActionItem>)
    suspend fun updateActionItem(item: ActionItem)
    suspend fun insertDecisions(decisions: List<Decision>)
    suspend fun insertMessageDrafts(drafts: List<MessageDraft>)
    suspend fun updateMessageDraft(draft: MessageDraft)
}
