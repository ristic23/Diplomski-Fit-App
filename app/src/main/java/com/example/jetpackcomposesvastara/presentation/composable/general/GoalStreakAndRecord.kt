package com.example.jetpackcomposesvastara.presentation.composable.general

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GoalStreakAndRecordCard(
    currStreak: Int,
    allTimeRecord: Int
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Daily stats",
                fontSize = 18.sp,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            /**Curr Streak*/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current streak",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = currStreak.toString(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onBackground
                )
            }
            /**All time record*/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(start = 20.dp, end = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "All time record",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = allTimeRecord.toString(),
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
    }
}