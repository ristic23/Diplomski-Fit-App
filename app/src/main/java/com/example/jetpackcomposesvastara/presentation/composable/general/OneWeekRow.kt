package com.example.jetpackcomposesvastara.presentation.composable.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.CalendarDayObject
import com.example.jetpackcomposesvastara.R
import java.util.*

@Composable
fun OneWeekRow(
    dayList: List<CalendarDayObject>,
    modifier: Modifier,
    contentPadding: Dp,
    dayGoal: Int
) {
    Row(
      modifier = modifier
    ) {
        dayList.forEach { day ->
            OneDayItem(
                oneDay = day,
                modifier = Modifier
                    .weight(1f)
                    .padding(contentPadding)
                    .fillMaxHeight(),
                dayGoal = dayGoal
            )
        }
    }
}

@Composable
private fun OneDayItem(
    oneDay: CalendarDayObject,
    modifier: Modifier,
    dayGoal: Int
)
{
    val notFocusedColorValue = Color(255, 255,255,128)
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
        ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        if (oneDay.stepAchieved >= dayGoal)
                            MaterialTheme.colors.primary
                        else
                            colorResource(id = R.color.transparent),
                        RoundedCornerShape(8.dp)
                    )
            )

            if(oneDay.day != -1)
                Text(
                    text = String.format(Locale.ROOT, "%02d", oneDay.day),
                    fontSize = 10.sp,
                    color = notFocusedColorValue,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 3.dp, top = 1.dp)
                )

            if(oneDay.day != -1)
                Text(
                    text = oneDay.stepAchieved.toString(),
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.onBackground
                )
        }
    }

}