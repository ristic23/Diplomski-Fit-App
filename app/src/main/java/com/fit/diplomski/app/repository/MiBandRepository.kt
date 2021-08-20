package com.fit.diplomski.app.repository

import androidx.annotation.WorkerThread
import com.fit.diplomski.app.database.EntryMiBand
import com.fit.diplomski.app.database.MiBandDAO
import kotlinx.coroutines.flow.Flow

class MiBandRepository(private val miBandDao: MiBandDAO) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allMiBandEntries: Flow<List<EntryMiBand>> = miBandDao.getAll()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(entryMiBand: EntryMiBand) {
        miBandDao.insert(entryMiBand)
    }
}