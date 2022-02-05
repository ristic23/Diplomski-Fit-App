package com.example.roomdb.converters

import com.example.common.JournalDataObject
import com.example.roomdb.entity.JournalEntity

fun mapJournalToObjectDTO(input: JournalEntity): JournalDataObject =
    JournalDataObject(
        uid = input.uid,
        isHydration = input.isHydration,
        hydrationValue = input.hydrationValue,
        journalTime = input.journalTime,
        hydrationDrinkName = input.hydrationDrinkName,
        activityStringValue = input.activityStringValue,
        stepsProgress = input.stepsProgress,
        calProgress = input.calProgress,
        kmProgress = input.kmProgress,
        date = input.date,
        activityDuration = input.activityDuration
    )

fun mapJournalToEntityDTO(input: JournalDataObject): JournalEntity =
    JournalEntity(
        uid = input.uid,
        isHydration = input.isHydration,
        hydrationValue = input.hydrationValue,
        journalTime = input.journalTime,
        hydrationDrinkName = input.hydrationDrinkName,
        activityStringValue = input.activityStringValue,
        stepsProgress = input.stepsProgress,
        calProgress = input.calProgress,
        kmProgress = input.kmProgress,
        date = input.date,
        activityDuration = input.activityDuration
    )