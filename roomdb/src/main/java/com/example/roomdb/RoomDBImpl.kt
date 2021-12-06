package com.example.roomdb

import com.example.common.GoalDataObject
import com.example.common.JournalDataObject
import com.example.roomdb.converters.mapGoalToEntityDTO
import com.example.roomdb.converters.mapJournalToEntityDTO
import com.example.roomdb.converters.mapGoalToObjectDTO
import com.example.roomdb.converters.mapJournalToObjectDTO
import com.example.roomdb.dao.GoalDAO
import com.example.roomdb.dao.JournalDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RoomDBImpl @Inject constructor(
    private val journalDAO: JournalDAO,
    private val goalDAO: GoalDAO,
)
{

    //region Journal
    suspend fun saveJournal(journalDataObject: JournalDataObject)
    {
        journalDAO.saveJournal(mapJournalToEntityDTO(journalDataObject))
    }

    suspend fun readSpecificJournal(uid: Int): JournalDataObject =
        mapJournalToObjectDTO(journalDAO.getSpecificJournalEntry(uid))

    suspend fun deleteJournal(journalDataObject: JournalDataObject)
    {
        journalDAO.deleteJournal(mapJournalToEntityDTO(journalDataObject))
    }

    suspend fun getAllJournals(): Flow<List<JournalDataObject>> = flow {
        journalDAO.getAll().collect { list ->
            emit(
                list.map { entity ->
                    mapJournalToObjectDTO(entity)
                }
            )
        }
    }
    //endregion

    //region Gaol
    suspend fun saveGoal(goalDataObject: GoalDataObject)
    {
        goalDAO.saveNewGoal(mapGoalToEntityDTO(goalDataObject))
    }

    suspend fun readSpecificGoal(uid: Int): GoalDataObject =
        mapGoalToObjectDTO(goalDAO.getSpecificGoal(uid))

    suspend fun deleteGoal(goalDataObject: GoalDataObject)
    {
        goalDAO.deleteGoal(mapGoalToEntityDTO(goalDataObject))
    }

    suspend fun getAllGoals(): Flow<List<GoalDataObject>> = flow {
        goalDAO.getAllGoals().collect { list ->
            emit(
                list.map { entity ->
                    mapGoalToObjectDTO(entity)
                }
            )
        }
    }
    //endregion




}