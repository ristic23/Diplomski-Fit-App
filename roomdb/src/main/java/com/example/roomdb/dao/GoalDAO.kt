package com.example.roomdb.dao

import androidx.room.*
import com.example.roomdb.entity.GoalEntity
import com.example.roomdb.utils.GOAL_TABLE_TIME
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalDAO {

    @Query("SELECT * FROM $GOAL_TABLE_TIME where uid LIKE :uid")
    suspend fun getSpecificGoal(uid: Int): GoalEntity

    @Query("SELECT * FROM $GOAL_TABLE_TIME")
    fun getAllGoals(): Flow<List<GoalEntity>>

    @Update
    suspend fun updateGoal(journalEntity: GoalEntity)

    @Insert
    suspend fun saveNewGoal(journalEntity: GoalEntity)

    @Delete
    suspend fun deleteGoal(journalEntity: GoalEntity)


}