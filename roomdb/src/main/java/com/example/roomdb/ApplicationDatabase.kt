package com.example.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.roomdb.converters.TypeConverter
import com.example.roomdb.dao.GoalDAO
import com.example.roomdb.dao.JournalDAO
import com.example.roomdb.entity.GoalEntity
import com.example.roomdb.entity.JournalEntity

@Database(
    entities = [JournalEntity::class, GoalEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class ApplicationDatabase : RoomDatabase()
{
    abstract fun getJournalDao(): JournalDAO
    abstract fun getGoalDao(): GoalDAO

}