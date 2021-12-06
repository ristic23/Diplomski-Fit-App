package com.example.repository

import com.example.common.GoalDataObject
import com.example.common.JournalDataObject
import com.example.roomdb.RoomDBImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private val roomDbImpl: RoomDBImpl) : RepositoryInterface
{

    //region Journal

    override suspend fun saveJournal(journalDataObject: JournalDataObject) {
        roomDbImpl.saveJournal(journalDataObject)
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

}