package com.fit.diplomski.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "EntryMiBand")
class EntryMiBand(
    @PrimaryKey @ColumnInfo(name = "word") val word: String
    )