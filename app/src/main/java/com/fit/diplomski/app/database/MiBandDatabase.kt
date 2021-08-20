package com.fit.diplomski.app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EntryMiBand::class], version = 1, exportSchema = false)
public abstract class MiBandDatabase : RoomDatabase() {

    abstract fun miBandDao(): MiBandDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MiBandDatabase? = null

        fun getDatabase(context: Context): MiBandDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiBandDatabase::class.java,
                    "MiBandDatabase"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}