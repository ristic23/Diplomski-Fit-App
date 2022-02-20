package com.example.jetpackcomposesvastara.presentation.composable.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.example.common.CalendarDayObject
import com.example.jetpackcomposesvastara.R
import java.util.*

@Composable
fun OneWeekRow(
    dayList: List<CalendarDayObject>,
    modifier: Modifier,
    contentPadding: Dp
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
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun OneDayItem(
    oneDay: CalendarDayObject,
    modifier: Modifier
)
{
    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
        ){
            if(oneDay.isCompleted)
                Icon(
                    painter = painterResource(id = R.drawable.ic_circle),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier.fillMaxSize()
                )
            if(oneDay.day != -1)
                Text(
                    text = String.format(Locale.ROOT, "%02d", oneDay.day),
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onBackground
                )

        }
    }

}