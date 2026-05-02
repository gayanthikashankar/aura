package com.example.aura.di

import android.content.Context
import androidx.room.Room
import com.example.aura.data.local.AppDatabase
import com.example.aura.data.local.dao.MeetingDao
import com.example.aura.data.repository.AISourceImpl
import com.example.aura.data.repository.MeetingRepositoryImpl
import com.example.aura.domain.repository.AISource
import com.example.aura.domain.repository.MeetingRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "aura_database"
        ).build()
    }

    @Provides
    fun provideMeetingDao(appDatabase: AppDatabase): MeetingDao {
        return appDatabase.meetingDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        meetingRepositoryImpl: MeetingRepositoryImpl
    ): MeetingRepository

    @Binds
    @Singleton
    abstract fun bindAISource(
        aiSourceImpl: AISourceImpl
    ): AISource
}
