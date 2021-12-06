package com.example.common

import java.util.*

data class JournalDataObject (
    var uid: Int,
    var isHydration: Boolean,
    var hydrationValue: String,
    var activityStringValue: String,
    var stepsProgress: Int? = null,
    var calProgress: Int? = null,
    var kmProgress: Float? = null,
    var date: Date
    )