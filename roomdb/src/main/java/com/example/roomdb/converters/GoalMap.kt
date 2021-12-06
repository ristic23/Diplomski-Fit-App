package com.example.roomdb.converters

import com.example.common.GoalDataObject
import com.example.roomdb.entity.GoalEntity

fun mapGoalToObjectDTO(input: GoalEntity): GoalDataObject =
    GoalDataObject(
        input.uid,
        input.GoalType,
        input.stepProgress,
        input.stepGoal,
        input.date
    )

fun mapGoalToEntityDTO(input: GoalDataObject): GoalEntity =
    GoalEntity(
        input.uid,
        input.GoalType,
        input.stepProgress,
        input.stepGoal,
        input.date
    )