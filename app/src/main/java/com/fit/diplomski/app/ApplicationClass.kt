package com.fit.diplomski.app

import android.app.Application
import com.fit.diplomski.app.database.MiBandDatabase
import com.fit.diplomski.app.repository.MiBandRepository

class ApplicationClass: Application()
{

    val database by lazy { MiBandDatabase.getDatabase(this) }
    val repository by lazy { MiBandRepository(database.miBandDao()) }

}