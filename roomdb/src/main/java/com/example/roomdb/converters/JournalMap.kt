package com.example.roomdb.converters

import com.example.common.JournalDataObject
import com.example.roomdb.entity.JournalEntity

fun mapJournalToObjectDTO(input: JournalEntity): JournalDataObject =
    JournalDataObject(
        input.uid,
        input.isHydration,
        input.hydrationValue,
        input.activityStringValue,
        input.stepsProgress,
        input.calProgress,
        input.kmProgress,
        input.date
    )

fun mapJournalToEntityDTO(input: JournalDataObject): JournalEntity =
    JournalEntity(
        input.uid,
        input.isHydration,
        input.hydrationValue,
        input.activityStringValue,
        input.stepsProgress,
        input.calProgress,
        input.kmProgress,
        input.date
    )