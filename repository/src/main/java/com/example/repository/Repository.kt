package com.example.repository

import com.example.common.Gender
import com.example.common.GoalDataObject
import com.example.common.JournalDataObject
import com.example.datastore.DataStoreManager
import com.example.roomdb.RoomDBImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val roomDbImpl: RoomDBImpl,
    private val dataStoreManager: DataStoreManager
) : RepositoryInterface
{

    //region Journal

    override suspend fun saveJournal(journalDataObject: JournalDataObject) {
        journalDataObject.uid = 0
        roomDbImpl.saveJournal(journalDataObject)
    }

    override suspend fun updateJournal(journalDataObject: JournalDataObject) {
        roomDbImpl.updateJournal(journalDataObject)
    }

    override suspend fun readSpecificJournal(uid: Int): JournalDataObject =
        roomDbImpl.readSpecificJournal(uid)

    override suspend fun deleteJournal(journalDataObject: JournalDataObject) {
        roomDbImpl.deleteJournal(journalDataObject)
    }
    override suspend fun getAllJournals(): Flow<List<JournalDataObject>> =
        roomDbImpl.getAllJournals()

    //endregion

    //region Goal

    override suspend fun saveGoal(goalDataObject: GoalDataObject) {
        roomDbImpl.saveGoal(goalDataObject)
    }

    override suspend fun readSpecificGoal(uid: Int): GoalDataObject = roomDbImpl.readSpecificGoal(uid)

    override suspend fun getAllGoals(): Flow<List<GoalDataObject>> = roomDbImpl.getAllGoals()

    override suspend fun deleteGoal(goalDataObject: GoalDataObject) {
        roomDbImpl.deleteGoal(goalDataObject)
    }
    //endregion

    //region DataStore

    override fun currentStepGoalFlow(): Flow<Int> = dataStoreManager.currentStepGoalFlow

    override suspend fun setNewStepDailyGoal(newValue: Int) {
        dataStoreManager.setNewStepDailyGoal(newValue)
    }

    override fun currentFirstNameFlow(): Flow<String> = dataStoreManager.currentFirstNameFlow

    override suspend fun setNewFirstName(newValue: String) {
        dataStoreManager.setNewFirstName(newValue)
    }

    override fun currentLastNameFlow(): Flow<String> = dataStoreManager.currentLastNameFlow

    override suspend fun setNewLastName(newValue: String) {
        dataStoreManager.setNewLastName(newValue)
    }

    override fun currentGenderFlow(): Flow<Gender> = dataStoreManager.currentGenderFlow

    override suspend fun setNewGender(newValue: Gender) {
        dataStoreManager.setNewGender(newValue)
    }

    override fun currentBirthdayFlow(): Flow<String> = dataStoreManager.currentBirthdayFlow

    override suspend fun setNewBirthday(newValue: String) {
        dataStoreManager.setNewBirthday(newValue)
    }

    override fun currentWeightFlow(): Flow<Int> = dataStoreManager.currentWeightFlow

    override suspend fun setNewWeight(newValue: Int) {
        dataStoreManager.setNewWeight(newValue)
    }

    override fun currentHeightFlow(): Flow<Int> = dataStoreManager.currentHeightFlow

    override suspend fun setNewHeight(newValue: Int) {
        dataStoreManager.setNewHeight(newValue)
    }

    override fun currentLastTimeUpdateIsDoneFlow(): Flow<String> = dataStoreManager.currentLastTimeUpdateIsDoneFlow

    override suspend fun setNewLastTimeUpdateIsDone(newValue: String) {
        dataStoreManager.setNewLastTimeUpdateIsDone(newValue)
    }

    //endregion

}