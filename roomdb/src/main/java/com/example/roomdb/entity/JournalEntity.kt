package com.example.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.roomdb.utils.JOURNAL_TABLE_TIME
import java.util.*

@Entity(tableName = JOURNAL_TABLE_TIME)
data class JournalEntity (
    @PrimaryKey(autoGenerate = true) var uid: Int,
    @ColumnInfo(name = "IsHydration") var isHydration: Boolean,
    @ColumnInfo(name = "HydrationValue") var hydrationValue: String,
    @ColumnInfo(name = "JournalTime") var journalTime: String,
    @ColumnInfo(name = "HydrationDrinkName") var hydrationDrinkName: String,
    @ColumnInfo(name = "ActivityStringValue") var activityStringValue: String,
    @ColumnInfo(name = "StepsProgress") var stepsProgress: Int? = null,
    @ColumnInfo(name = "CalProgress") var calProgress: Int? = null,
    @ColumnInfo(name = "KmProgress") var kmProgress: Float? = null,
    @ColumnInfo(name = "Date") var date: Date


    )