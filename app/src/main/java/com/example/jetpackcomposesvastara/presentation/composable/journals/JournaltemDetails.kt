package com.example.jetpackcomposesvastara.presentation.composable.journals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.common.FitnessActivitiesDataObject
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.data.fitnessActivities.FitnessActivities
import com.example.jetpackcomposesvastara.presentation.composable.general.DetailsTopBar
import com.example.jetpackcomposesvastara.presentation.composable.general.CustomOutlinedTextField
import com.example.jetpackcomposesvastara.presentation.composable.general.RowWithDescAndAction

@Composable
fun JournalItemDetails(
    navController: NavController,
    uid: Int,
    identifier: String
) {
    val scrollState = rememberScrollState()
    var openDialog by remember { mutableStateOf(false)  }

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
                //todo
            },
            doneClicked = {
                //todo
            },
            uid = uid
        )
        if(identifier == "hydration")
        {
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
                value = "",
                onValueChange = {},
                fieldClicked = {},
                enabled = true
            )
            CustomOutlinedTextField(
                inputHint = "Drink Name",
                value = "",
                onValueChange = {},
                fieldClicked = {},
                enabled = true
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
        }
        else
        {
            //Workout
            CustomOutlinedTextField(
                inputHint = "Title",
                value = "",
                onValueChange = {},
                fieldClicked = {},
                enabled = true
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
            RowWithDescAndAction(
                value = "Running",
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
                value = "30",
                descAction = "Duration",
                suffix = "min",
                fieldClicked = {}
            )
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(id = R.color.MediumGray)
            )
            CustomOutlinedTextField(
                inputHint = "Distance in km",
                value = "",
                onValueChange = {},
                fieldClicked = {},
                enabled = true
            )
            CustomOutlinedTextField(
                inputHint = "Steps",
                value = "",
                onValueChange = {},
                fieldClicked = {},
                enabled = true
            )
            CustomOutlinedTextField(
                inputHint = "Calories",
                value = "",
                onValueChange = {},
                fieldClicked = {},
                enabled = true
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
                    //todo update UI for new activity
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


