package com.example.jetpackcomposesvastara.presentation.composable.journals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.common.FitnessActivitiesDataObject
import com.example.common.JournalDataObject
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.data.fitnessActivities.FitnessActivities
import com.example.jetpackcomposesvastara.presentation.composable.general.DetailsTopBar
import com.example.jetpackcomposesvastara.presentation.composable.general.CustomOutlinedTextField
import com.example.jetpackcomposesvastara.presentation.composable.general.RowWithDescAndAction

@Composable
fun JournalItemDetails(
    navController: NavController,
    uid: Int,
    identifier: String,
    viewModel: JournalItemDetailsViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    var openDialog by remember { mutableStateOf(false)  }
    var uidRemember by remember { mutableStateOf(uid)  }

    val journalDataObject by viewModel.journalDataObject.observeAsState(JournalDataObject())
    if(uidRemember != -1)
        viewModel.getSpecificJournalDataObject(uidRemember)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        DetailsTopBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            text =  when(identifier) {
                "hydration" -> "Hydration"
                "workout" -> "Workout"
                else -> "Error"
            },
            deleteClicked = {
                viewModel.deleteJournalDataObject(journalDataObject) {
                    uidRemember = -1
                    navController.popBackStack()
                }
            },
            doneClicked = {
                viewModel.saveOrUpdateJournalDataObject(journalDataObject) {
                    uidRemember = -1
                    navController.popBackStack()
                }
            },
            uid = uidRemember
        )
        if(identifier == "hydration")
        {
            journalDataObject.isHydration = true
            //Hydration
            RowWithDescAndAction(
                value = "14:44",
                descAction = "Time",
                fieldClicked = {}
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
            CustomOutlinedTextField(
                inputHint = "Add ml",
                value = journalDataObject.hydrationValue,
                onValueChange = {
                    journalDataObject.hydrationValue = it
                },
                fieldClicked = {},
                enabled = true,
                keyboardType = KeyboardType.Number
            )
            CustomOutlinedTextField(
                inputHint = "Drink Name",
                value = journalDataObject.hydrationDrinkName,
                onValueChange = {
                    journalDataObject.hydrationDrinkName = it
                },
                fieldClicked = {},
                enabled = true,
                keyboardType = KeyboardType.Text
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
        }
        else
        {
            journalDataObject.isHydration = false
            //Workout
            CustomOutlinedTextField(
                inputHint = "Title",
                value = journalDataObject.hydrationDrinkName,
                onValueChange = {
                    journalDataObject.hydrationDrinkName = it
                },
                fieldClicked = {},
                enabled = true,
                keyboardType = KeyboardType.Text
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
            RowWithDescAndAction(
                value = journalDataObject.activityStringValue,
                descAction = "Activity",
                fieldClicked = {
                    openDialog = true
                }
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
            RowWithDescAndAction(
                value = "14:44",
                descAction = "Start",
                fieldClicked = {}
            )
            RowWithDescAndAction(
                value = journalDataObject.activityDuration.toString(),
                descAction = "Duration",
                suffix = "min",
                fieldClicked = {}
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
            if(journalDataObject.kmProgress != null)
                CustomOutlinedTextField(
                    inputHint = "Distance in km",
                    value = if(journalDataObject.kmProgress == 0f) "" else journalDataObject.kmProgress.toString(),
                    onValueChange = {
                        journalDataObject.kmProgress = if(it.isEmpty()) null else it.toFloat()
                    },
                    fieldClicked = {},
                    enabled = true,
                    keyboardType = KeyboardType.Number
                )
            if(journalDataObject.stepsProgress != null)
                CustomOutlinedTextField(
                    inputHint = "Steps",
                    value = if(journalDataObject.stepsProgress == 0) "" else journalDataObject.stepsProgress.toString(),
                    onValueChange = {
                        journalDataObject.stepsProgress = if(it.isEmpty()) null else it.toInt()
                    },
                    fieldClicked = {},
                    enabled = true,
                    keyboardType = KeyboardType.Number
                )
            if(journalDataObject.calProgress != null)
                CustomOutlinedTextField(
                    inputHint = "Calories",
                    value = if(journalDataObject.calProgress == 0) "" else journalDataObject.calProgress.toString(),
                    onValueChange = {
                        journalDataObject.calProgress = if(it.isEmpty()) null else it.toInt()
                    },
                    fieldClicked = {},
                    enabled = true,
                    keyboardType = KeyboardType.Number
                )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
        }

        if (openDialog) {
            ShowActivitiesDialog(
                onDismissRequest = {
                    openDialog = false
                },
                onItemClicked = {
                    journalDataObject.activityStringValue = it.activityName
                    journalDataObject.kmProgress = it.kmProgress
                    journalDataObject.calProgress = it.calProgress
                    journalDataObject.stepsProgress = it.stepsProgress
                    openDialog = false
                }
            )
        }
    }
}

@Composable
fun ShowActivitiesDialog(
    onDismissRequest: () -> Unit,
    onItemClicked: (FitnessActivitiesDataObject) -> Unit
)
{
    Dialog(
        onDismissRequest = {
            onDismissRequest.invoke()
        },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.9f)
                    .padding(8.dp)
            )
            {
                Card(
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(15.dp))
                        Text(
                            text = "Choose Activity",
                            color = MaterialTheme.colors.onBackground,
                            fontSize = 25.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp))
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colors.onSurface
                        )
                        Spacer(modifier = Modifier
                            .fillMaxWidth()
                            .height(5.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(8.dp)
                        )
                        {
                            items(
                                count = FitnessActivities.activityList.size,
                                itemContent = { index ->
                                    Column(
                                        modifier = Modifier
                                            .clickable {
                                                onItemClicked(FitnessActivities.activityList[index])
                                            }
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                    ) {
                                        Text(
                                            text = FitnessActivities.activityList[index].activityName,
                                            fontSize = 20.sp,
                                            color = MaterialTheme.colors.onBackground,
                                        )
                                        Spacer(modifier = Modifier
                                            .fillMaxWidth()
                                            .height(5.dp))
                                        Divider(
                                            modifier = Modifier.fillMaxWidth(),
                                            color = MaterialTheme.colors.onSurface
                                        )
                                        Spacer(modifier = Modifier
                                            .fillMaxWidth()
                                            .height(5.dp))
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}


