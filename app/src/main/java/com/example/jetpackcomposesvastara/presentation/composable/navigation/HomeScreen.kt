package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.presentation.composable.LinearProgressBar
import com.example.jetpackcomposesvastara.util.Constants.cardElevation
import com.example.jetpackcomposesvastara.util.Constants.largeRoundedCorner

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollState)
    ) {
        CurrentProgressCard()
    }

}

@Composable
fun CurrentProgressCard() {
    Surface(
        shape = RoundedCornerShape(largeRoundedCorner),
        elevation = cardElevation,
        modifier = Modifier
                .padding(20.dp),
        color = MaterialTheme.colors.surface)
    {
        Column {
            Spacer(modifier = Modifier.height(15.dp))
            LinearProgressBar(
                maxValue = 10000,
                progressValue = 5646,
                progressColor = colorResource(id = R.color.steps_progress),
                bgColor = colorResource(id = R.color.progress_bg),
                textColor = colorResource(id = R.color.steps_progress),
                text = "Steps"
            )
            Spacer(modifier = Modifier.height(15.dp))
            LinearProgressBar(
                maxValue = 100,
                progressValue = 66,
                progressColor = colorResource(id = R.color.cal_progress),
                bgColor = colorResource(id = R.color.progress_bg),
                textColor = colorResource(id = R.color.cal_progress),
                text = "Calories"
            )
            Spacer(modifier = Modifier.height(15.dp))
            LinearProgressBar(
                maxValue = 10000,
                progressValue = 2547,
                progressColor = colorResource(id = R.color.distance_progress),
                bgColor = colorResource(id = R.color.progress_bg),
                textColor = colorResource(id = R.color.distance_progress),
                text = "Distances"
            )
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}