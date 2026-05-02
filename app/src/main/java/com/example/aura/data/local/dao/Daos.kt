package com.example.aura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.aura.data.local.entity.ActionItemEntity
import com.example.aura.data.local.entity.DecisionEntity
import com.example.aura.data.local.entity.MeetingEntity
import com.example.aura.data.local.entity.MessageDraftEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface MeetingDao {
    @Query("SELECT * FROM meetings ORDER BY recordedAt DESC")
    fun getAllMeetings(): Flow<List<MeetingEntity>>

    @Query("SELECT * FROM meetings WHERE id = :id")
    fun getMeetingById(id: UUID): Flow<MeetingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeeting(meeting: MeetingEntity)

    @Update
    suspend fun updateMeeting(meeting: MeetingEntity)

    @Query("SELECT * FROM action_items WHERE meetingId = :meetingId ORDER BY priority DESC")
    fun getActionItemsForMeeting(meetingId: UUID): Flow<List<ActionItemEntity>>

    @Query("SELECT * FROM action_items ORDER BY deadline ASC")
    fun getAllActionItems(): Flow<List<ActionItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActionItems(items: List<ActionItemEntity>)

    @Update
    suspend fun updateActionItem(item: ActionItemEntity)

    @Query("SELECT * FROM decisions WHERE meetingId = :meetingId")
    fun getDecisionsForMeeting(meetingId: UUID): Flow<List<DecisionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDecisions(decisions: List<DecisionEntity>)

    @Query("SELECT * FROM message_drafts WHERE meetingId = :meetingId")
    fun getMessageDraftsForMeeting(meetingId: UUID): Flow<List<MessageDraftEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessageDrafts(drafts: List<MessageDraftEntity>)

    @Update
    suspend fun updateMessageDraft(draft: MessageDraftEntity)
}
