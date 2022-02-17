package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpackcomposesvastara.presentation.viewModel.GoalViewModel


@ExperimentalComposeUiApi
@Composable
fun GoalsScreen() {

    val viewModel: GoalViewModel = hiltViewModel()

    val stepDailyGoal by viewModel.stepsLiveData.observeAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        val profileRowModifier = Modifier
            .padding(5.dp)
            .weight(1f)

        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "Steps",
                value = stepDailyGoal.toString(),
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                updateValue = {
//                    if(it.isEmpty()) viewModel.stepsLiveData.postValue(0)
                },
                notFocusedListener = {
                    viewModel.setNewStepGoal(if(it.isEmpty()) 0 else it.toInt())
                    if(it.isEmpty()) viewModel.stepsLiveData.postValue(0)
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}
