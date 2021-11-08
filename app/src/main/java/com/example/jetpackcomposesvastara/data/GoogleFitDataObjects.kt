package com.example.jetpackcomposesvastara.data

import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType

 val TYPE_STEP_COUNT_DELTA = DataType.TYPE_STEP_COUNT_DELTA
 val AGGREGATE_STEP_COUNT_DELTA = DataType.AGGREGATE_STEP_COUNT_DELTA
 val TYPE_CALORIES_EXPENDED = DataType.TYPE_CALORIES_EXPENDED
 val AGGREGATE_CALORIES_EXPENDED = DataType.AGGREGATE_CALORIES_EXPENDED
 val TYPE_DISTANCE_DELTA = DataType.TYPE_DISTANCE_DELTA
 val AGGREGATE_DISTANCE_DELTA = DataType.AGGREGATE_DISTANCE_DELTA



 val fitnessOption = FitnessOptions.builder()
  .addDataType(TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
  .addDataType(AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
  .addDataType(TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
  .addDataType(AGGREGATE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
  .addDataType(TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
  .addDataType(AGGREGATE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
  .build()