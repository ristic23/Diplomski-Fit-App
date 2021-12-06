package com.example.common

//todo treba i entitet u bazu da napravim koji ce da cuva sve ovo plus title, time, note mozda
data class FitnessActivitiesDataObject(
    val activityName: String,
    val activityStringValue: String,
    var stepsProgress: Int? = null,
    var calProgress: Int? = null,
    var kmProgress: Float? = null

)
