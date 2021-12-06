package com.example.repository

import com.example.common.GoalDataObject
import com.example.common.JournalDataObject
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface
{

    //region Journal
    suspend fun saveJournal(journalDataObject: JournalDataObject)

    suspend fun readSpecificJournal(uid: Int): JournalDataObject

    suspend fun deleteJournal(journalDataObject: JournalDataObject)

    suspend fun getAllJournals(): Flow<List<JournalDataObject>>
    //endregion

    //region Goal
    suspend fun saveGoal(goalDataObject: GoalDataObject)

    suspend fun readSpecificGoal(uid: Int): GoalDataObject

    suspend fun deleteGoal(goalDataObject: GoalDataObject)

    suspend fun getAllGoals(): Flow<List<GoalDataObject>>
    //endregion

}