package com.example.roomdb.dao

import androidx.room.*
import com.example.roomdb.entity.JournalEntity
import com.example.roomdb.utils.JOURNAL_TABLE_TIME
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDAO {

    @Query("SELECT * FROM $JOURNAL_TABLE_TIME where uid LIKE :uid")
    suspend fun getSpecificJournalEntry(uid: Int): JournalEntity

    @Query("SELECT * FROM $JOURNAL_TABLE_TIME")
    fun getAll(): Flow<List<JournalEntity>>

    @Update
    suspend fun updateJournal(journalEntity: JournalEntity)

    @Insert
    suspend fun saveJournal(journalEntity: JournalEntity)

    @Delete
    suspend fun deleteJournal(journalEntity: JournalEntity)


}