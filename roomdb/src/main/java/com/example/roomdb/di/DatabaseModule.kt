package com.example.roomdb.di

import android.content.Context
import androidx.room.Room
import com.example.roomdb.ApplicationDatabase
import com.example.roomdb.dao.GoalDAO
import com.example.roomdb.dao.JournalDAO
import com.example.roomdb.utils.APPLICATION_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideJournalDao(database: ApplicationDatabase): JournalDAO =
        database.getJournalDao()

    @Provides
    fun provideGoalDao(database: ApplicationDatabase): GoalDAO =
        database.getGoalDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ApplicationDatabase =
        Room.databaseBuilder(
            context,
            ApplicationDatabase::class.java,
            APPLICATION_DATABASE_NAME
        ).build()

}