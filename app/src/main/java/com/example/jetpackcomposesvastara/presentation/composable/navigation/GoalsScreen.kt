package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jetpackcomposesvastara.presentation.composable.general.GoalStreakAndRecordCard
import com.example.jetpackcomposesvastara.presentation.composable.general.OneWeekRow
import com.example.jetpackcomposesvastara.presentation.viewModel.GoalViewModel


@ExperimentalComposeUiApi
@Composable
fun GoalsScreen() {

    val viewModel: GoalViewModel = hiltViewModel()

    val stepDailyGoal by viewModel.stepsLiveData.observeAsState(0)
    val weekRangeLiveData by viewModel.weekRangeLiveData.observeAsState("")

    val items by viewModel.weekListData.observeAsState(mutableListOf())

    val currStreak by viewModel.currStreakLiveData.observeAsState(initial = 0)
    val allTimeRecord by viewModel.allTimeRecordLiveData.observeAsState(initial = 0)

    val dayNames: List<String> = listOf(
        "MON",
        "TUE",
        "WED",
        "THU",
        "FRI",
        "SAT",
        "SUN",
    )

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

        GoalStreakAndRecordCard(currStreak = currStreak, allTimeRecord = allTimeRecord)

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Week History",
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                /**Week and arrows indicator*/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .height(30.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.prevWeek()
                            }
                    )
                    Text(
                        text = weekRangeLiveData,
                        //            fontSize = 24.sp,
                        color = MaterialTheme.colors.onBackground
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .height(30.dp)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .clickable {
                                viewModel.nextWeek()
                            }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                /**Day indicators*/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                ) {
                    dayNames.forEach { day ->
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                /**Week date and is it completed indicator*/
                OneWeekRow(
                    dayList = items,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentPadding = 4.dp,
                    dayGoal = stepDailyGoal
                )
            }
        }

    }
}
