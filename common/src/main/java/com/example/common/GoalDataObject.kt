package com.example.common

import java.util.*

data class GoalDataObject (
    var uid: Int,
    var GoalType: GoalType,
    var stepProgress: Int,
    var stepGoal: Int,
    var date: Date
    )