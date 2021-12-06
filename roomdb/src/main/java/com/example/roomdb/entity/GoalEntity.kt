package com.example.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.common.GoalType
import com.example.roomdb.utils.JOURNAL_TABLE_TIME
import java.util.*

@Entity(tableName = JOURNAL_TABLE_TIME)
data class GoalEntity (
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "GoalType") var GoalType: GoalType,
    @ColumnInfo(name = "StepProgress") var stepProgress: Int = 0,
    @ColumnInfo(name = "StepGoal") var stepGoal: Int = 1000,
    @ColumnInfo(name = "Date") var date: Date


    )