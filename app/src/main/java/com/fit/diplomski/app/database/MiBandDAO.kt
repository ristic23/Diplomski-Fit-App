package com.fit.diplomski.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MiBandDAO {

    @Query("SELECT * FROM EntryMiBand ORDER BY word ASC")
    fun getAll(): Flow<List<EntryMiBand>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: EntryMiBand)

    @Query("DELETE FROM EntryMiBand")
    suspend fun deleteAll()
}