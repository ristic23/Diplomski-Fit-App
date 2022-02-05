package com.example.common

import java.util.*

data class JournalDataObject (
    var uid: Int = -1,
    var isHydration: Boolean = true,
    var hydrationValue: String = "",
    var journalTime: String = "",
    var hydrationDrinkName: String = "",
    var activityStringValue: String = "",
    var activityDuration: Int = 0,
    var stepsProgress: Int? = null,
    var calProgress: Int? = null,
    var kmProgress: Float? = null,
    var date: Date = Date()
    )