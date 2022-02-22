package com.example.jetpackcomposesvastara.presentation.composable.journals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.JournalDataObject
import com.example.jetpackcomposesvastara.presentation.composable.journals.RowJournalDesc
import com.example.jetpackcomposesvastara.presentation.composable.journals.formatDate
import com.example.jetpackcomposesvastara.util.Constants
import java.util.*


@Composable
fun JournalWorkoutCard(
    journalDataObject: JournalDataObject,
    itemClicked: (JournalDataObject) -> Unit
)
{
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 8.dp, bottom = 8.dp)
            .clickable {
                itemClicked.invoke(journalDataObject)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = journalDataObject.activityStringValue,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = formatDate(journalDataObject.date),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))

            RowJournalDesc(
                descAction = "Time",
                value = journalDataObject.journalTime,
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

            if(journalDataObject.stepsProgress != null) {
                RowJournalDesc(
                    descAction = "Steps",
                    value = (journalDataObject.stepsProgress ?: 0).toString(),
                    modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
            }
            if(journalDataObject.calProgress != null) {
                RowJournalDesc(
                    descAction = "Energy expended",
                    value = (journalDataObject.calProgress ?: 0).toString(),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = "cal")

                Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
            }

            if(journalDataObject.kmProgress != null) {
                RowJournalDesc(
                    descAction = "Distance",
                    value = (journalDataObject.kmProgress ?: 0).toString(),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = "km")

                Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
            }
        }
    }
}

@Preview
@Composable
fun JournalWorkoutCardPreview()
{
    val journalDataObject = JournalDataObject(
        uid = 1,
        isHydration = false,
        hydrationValue = "",
        journalTime = "11:45",
        hydrationDrinkName = "",
        activityStringValue = "Arm Wrestling",
        calProgress = 127,
        date = Calendar.getInstance(Locale.GERMANY).time
    )
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = journalDataObject.activityStringValue,
                    fontSize = 24.sp,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = formatDate(journalDataObject.date),
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onBackground
                )
            }
            Spacer(modifier = Modifier.fillMaxWidth().height(10.dp))

            RowJournalDesc(
                descAction = "Time",
                value = journalDataObject.journalTime,
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

            if(journalDataObject.stepsProgress != null) {
                RowJournalDesc(
                    descAction = "Steps",
                    value = (journalDataObject.stepsProgress ?: 0).toString(),
                    modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
            }
            if(journalDataObject.calProgress != null) {
                RowJournalDesc(
                    descAction = "Energy expended",
                    value = (journalDataObject.calProgress ?: 0).toString(),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = "cal")

                Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
            }

            if(journalDataObject.kmProgress != null) {
                RowJournalDesc(
                    descAction = "Distance",
                    value = (journalDataObject.kmProgress ?: 0).toString(),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = "km")

                Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
            }
        }
    }
}