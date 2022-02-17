package com.example.repository

import com.example.common.Gender
import com.example.common.GoalDataObject
import com.example.common.JournalDataObject
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface
{

    //region Journal
    suspend fun saveJournal(journalDataObject: JournalDataObject)

    suspend fun updateJournal(journalDataObject: JournalDataObject)

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

    //region DataStore

    fun currentStepGoalFlow() : Flow<Int>
    suspend fun setNewStepDailyGoal(newValue: Int)

    fun currentFirstNameFlow() : Flow<String>
    suspend fun setNewFirstName(newValue: String)

    fun currentLastNameFlow() : Flow<String>
    suspend fun setNewLastName(newValue: String)

    fun currentGenderFlow() : Flow<Gender>
    suspend fun setNewGender(newValue: Gender)

    fun currentBirthdayFlow() : Flow<String>
    suspend fun setNewBirthday(newValue: String)

    fun currentWeightFlow() : Flow<Int>
    suspend fun setNewWeight(newValue: Int)

    fun currentHeightFlow() : Flow<Int>
    suspend fun setNewHeight(newValue: Int)

    fun currentLastTimeUpdateIsDoneFlow() : Flow<String>
    suspend fun setNewLastTimeUpdateIsDone(newValue: String)

    //endregion

}