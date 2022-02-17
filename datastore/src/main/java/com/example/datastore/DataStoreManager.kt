package com.example.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.common.Gender
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val DATA_STORE_NAME = "RisticFitDataStore"

const val KEY_STEP_DAILY_GOAL = "KEY_STEP_DAILY_GOAL"
const val KEY_PROFILE_FIRST_NAME = "KEY_PROFILE_FIRST_NAME"
const val KEY_PROFILE_LAST_NAME = "KEY_PROFILE_LAST_NAME"
const val KEY_PROFILE_GENDER = "KEY_PROFILE_GENDER"
const val KEY_PROFILE_BIRTHDAY = "KEY_PROFILE_BIRTHDAY"
const val KEY_PROFILE_WEIGHT = "KEY_PROFILE_WEIGHT"
const val KEY_PROFILE_HEIGHT = "KEY_PROFILE_HEIGHT"

const val KEY_TIME_OF_LAST_GOAL_DB_UPDATE = "KEY_TIME_OF_LAST_GOAL_DB_UPDATE" // hh.mm.day.month.year


private val Context.risticFitDataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context)
{

    companion object {
        val STEP_COUNT_VALUE : Preferences.Key<Int> = intPreferencesKey(KEY_STEP_DAILY_GOAL)

        val FIRST_NAME : Preferences.Key<String> = stringPreferencesKey(KEY_PROFILE_FIRST_NAME)
        val LAST_NAME : Preferences.Key<String> = stringPreferencesKey(KEY_PROFILE_LAST_NAME)
        val GENDER : Preferences.Key<Int> = intPreferencesKey(KEY_PROFILE_GENDER)
        val BIRTHDAY : Preferences.Key<String> = stringPreferencesKey(KEY_PROFILE_BIRTHDAY)
        val WEIGHT : Preferences.Key<Int> = intPreferencesKey(KEY_PROFILE_WEIGHT)
        val HEIGHT : Preferences.Key<Int> = intPreferencesKey(KEY_PROFILE_HEIGHT)

        val TIME_LAST_GOAL_UPDATE : Preferences.Key<String> = stringPreferencesKey(KEY_TIME_OF_LAST_GOAL_DB_UPDATE)

    }

    private val risticDataStore: DataStore<Preferences> = context.risticFitDataStore


    suspend fun setNewStepDailyGoal(step: Int) {
        risticDataStore.edit {
            it[STEP_COUNT_VALUE] = step
        }
    }

    val currentStepGoalFlow: Flow<Int> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        preference[STEP_COUNT_VALUE] ?: 5000
    }

    suspend fun setNewFirstName(name: String) {
        risticDataStore.edit {
            it[FIRST_NAME] = name
        }
    }

    val currentFirstNameFlow: Flow<String> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        preference[FIRST_NAME] ?: ""
    }

    suspend fun setNewLastName(name: String) {
        risticDataStore.edit {
            it[LAST_NAME] = name
        }
    }

    val currentLastNameFlow: Flow<String> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        preference[LAST_NAME] ?: ""
    }

    suspend fun setNewGender(gender: Gender) {
        risticDataStore.edit {
            it[GENDER] = when(gender){
                Gender.Female -> 1
                Gender.Male -> 2
            }
        }
    }

    val currentGenderFlow: Flow<Gender> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        when(preference[GENDER]){
            1 -> Gender.Female
            2 -> Gender.Male
            else -> Gender.Male
        }
    }

    suspend fun setNewBirthday(name: String) {
        risticDataStore.edit {
            it[BIRTHDAY] = name
        }
    }

    val currentBirthdayFlow: Flow<String> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        preference[BIRTHDAY] ?: ""
    }

    suspend fun setNewWeight(name: Int) {
        risticDataStore.edit {
            it[WEIGHT] = name
        }
    }

    val currentWeightFlow: Flow<Int> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        preference[WEIGHT] ?: 0
    }

    suspend fun setNewHeight(name: Int) {
        risticDataStore.edit {
            it[HEIGHT] = name
        }
    }

    val currentHeightFlow: Flow<Int> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        preference[HEIGHT] ?: 0
    }

    suspend fun setNewLastTimeUpdateIsDone(name: String) {
        risticDataStore.edit {
            it[TIME_LAST_GOAL_UPDATE] = name
        }
    }

    val currentLastTimeUpdateIsDoneFlow: Flow<String> = risticDataStore.data.catch {
        if(it is IOException)
            emit(emptyPreferences())
        else
            throw(it)
    }.map { preference ->
        preference[TIME_LAST_GOAL_UPDATE] ?: ""
    }



}