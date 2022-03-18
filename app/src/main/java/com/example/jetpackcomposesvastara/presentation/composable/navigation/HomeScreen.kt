package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.presentation.composable.CircleProgressBar
import com.example.jetpackcomposesvastara.presentation.composable.LinearProgressBar
import com.example.jetpackcomposesvastara.presentation.composable.general.GoalStreakAndRecordCard
import com.example.jetpackcomposesvastara.presentation.viewModel.MainViewModel
import com.example.jetpackcomposesvastara.util.Constants.cardElevation
import com.example.jetpackcomposesvastara.util.Constants.largeRoundedCorner


@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    val viewModel : MainViewModel = hiltViewModel()

//    val viewModel = viewModel(MainViewModel::class.java)

    val currStreak by viewModel.currStreakLiveData.observeAsState(initial = 0)
    val allTimeRecord by viewModel.allTimeRecordLiveData.observeAsState(initial = 0)

    val stepsNumber by viewModel.stepsLiveData.observeAsState(initial = 0)
    val stepsGoalNumber by viewModel.stepsGoalLiveData.observeAsState(initial = 6000)

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            CircleProgressBar(
                indicatorValue = stepsNumber,
                maxIndicatorValue = stepsGoalNumber,
                foregroundIndicatorColor = colorResource(id = R.color.steps_progress),
                backgroundIndicatorColor = colorResource(id = R.color.progress_bg)
            )
        }

        CurrentProgressCard(viewModel)

        GoalStreakAndRecordCard(currStreak = currStreak, allTimeRecord = allTimeRecord)
    }

}

@Composable
fun CurrentProgressCard(viewModel: MainViewModel) {

//    val stepsNumber = viewModel.stepsLiveData.observeAsState(0).value
    val caloriesNumber = viewModel.caloriesLiveData.observeAsState(initial = 0).value
    val distanceNumber = viewModel.distanceLiveData.observeAsState(initial = 0).value
//    val stepsGoalNumber = viewModel.stepsGoalLiveData.observeAsState(initial = 0).value

    Column {
//        Surface(
//            shape = RoundedCornerShape(largeRoundedCorner),
//            elevation = cardElevation,
//            modifier = Modifier
//                .padding(start = 10.dp, end = 10.dp, top = 20.dp),
//            color = MaterialTheme.colors.surface)
//        {
//            Column {
//                Spacer(modifier = Modifier.height(15.dp))
//                LinearProgressBar(
//                    progress = (stepsNumber.toFloat() / stepsGoalNumber),
//                    progressText = "$stepsNumber / $stepsGoalNumber",
//                    progressColor = colorResource(id = R.color.steps_progress),
//                    bgColor = colorResource(id = R.color.progress_bg),
//                    textColor = colorResource(id = R.color.steps_progress),
//                    text = "Steps",
//                    isLinearProgressIndicatorVisible = true
//                )
//                Spacer(modifier = Modifier.height(15.dp))
//            }
//        }
        Surface(
            shape = RoundedCornerShape(largeRoundedCorner),
            elevation = cardElevation,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            color = MaterialTheme.colors.surface)
        {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressBar(
                    progress = 1f,
                    progressText = "$caloriesNumber Cal",
                    progressColor = colorResource(id = R.color.cal_progress),
                    bgColor = colorResource(id = R.color.progress_bg),
                    textColor = colorResource(id = R.color.cal_progress),
                    text = "Calories",
                    isLinearProgressIndicatorVisible = false
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Surface(
            shape = RoundedCornerShape(largeRoundedCorner),
            elevation = cardElevation,
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 10.dp),
            color = MaterialTheme.colors.surface)
        {
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressBar(
                    progress = 1f,
                    progressText = "${distanceNumber * 0.001f} km",
                    progressColor = colorResource(id = R.color.distance_progress),
                    bgColor = colorResource(id = R.color.progress_bg),
                    textColor = colorResource(id = R.color.distance_progress),
                    text = "Distances",
                    isLinearProgressIndicatorVisible = false
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

    }
}