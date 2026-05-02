package com.example.aura.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aura.data.local.dao.MeetingDao
import com.example.aura.data.local.entity.ActionItemEntity
import com.example.aura.data.local.entity.DecisionEntity
import com.example.aura.data.local.entity.MeetingEntity
import com.example.aura.data.local.entity.MessageDraftEntity

@Database(
    entities = [
        MeetingEntity::class,
        ActionItemEntity::class,
        DecisionEntity::class,
        MessageDraftEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun meetingDao(): MeetingDao
}
