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
import java.util.*

@Composable
fun JournalHydrationCard(
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
                    text = "Hydration",
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

            RowJournalDesc(
                descAction = "Drink",
                value = journalDataObject.hydrationDrinkName,
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

            RowJournalDesc(
                descAction = "Amount",
                value = journalDataObject.hydrationValue,
                modifier = Modifier.fillMaxWidth(),
                suffix = "ml")

            Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
        }
    }
}

@Composable
fun RowJournalDesc(
    descAction: String,
    value: String,
    suffix: String = "",
    modifier: Modifier
)
{
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(
            text = descAction,
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        )

        Text(
            text = "$value $suffix",
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        )
    }
}

fun formatDate(date: Date): String
{
    val calendar = Calendar.getInstance()
    calendar.time = date
    return String.format(Locale.ROOT, "%02d.%02d.%02d",
        calendar.get(Calendar.DAY_OF_MONTH),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.YEAR))
}


@Preview
@Composable
fun JournalHydrationCardPreview()
{
    val journalDataObject = JournalDataObject(
        uid = 1,
        isHydration = true,
        hydrationValue = "500",
        journalTime = "11:45",
        hydrationDrinkName = "Water",
        activityStringValue = "",
        stepsProgress = 0,
        calProgress = 0,
        kmProgress = 0f,
        date = Calendar.getInstance().time
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
                    text = "Hydration",
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

            RowJournalDesc(
                descAction = "Drink",
                value = journalDataObject.hydrationDrinkName,
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))

            RowJournalDesc(
                descAction = "Amount",
                value = journalDataObject.hydrationValue,
                modifier = Modifier.fillMaxWidth(),
                suffix = "ml")

            Spacer(modifier = Modifier.fillMaxWidth().height(8.dp))
        }
    }
}