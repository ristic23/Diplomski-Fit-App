package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.presentation.composable.util.ProfileClickData
import com.example.jetpackcomposesvastara.presentation.composable.util.ProfileClickState

//val context = LocalContext.current

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@Composable
fun ProfileScreen() {
    val profileClickData = remember { mutableStateOf(ProfileClickData()) }
    val dialogIsShowing = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize().wrapContentSize(Alignment.Center)
    ) {
        if(dialogIsShowing.value)
        {
            AlertDialog(profileClickData = profileClickData.value){ newProfileClickData, updateUI ->
                if(updateUI) {
                    profileClickData.value = newProfileClickData
                }
                dialogIsShowing.value = false
            }
        }



        val profileRowModifier = Modifier
            .padding(5.dp)
            .weight(1f)

        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "Steps",
                value = profileClickData.value.stepsValue.toString(),
                fieldClicked = {
                    profileClickData.value.profileClickState = ProfileClickState.STEPS
                    dialogIsShowing.value = true
                },
            modifier = profileRowModifier)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "First Name",
                value = profileClickData.value.firstName,
                fieldClicked = {
                    profileClickData.value.profileClickState = ProfileClickState.FIRST_NAME
                    dialogIsShowing.value = true
                },
                modifier = profileRowModifier)
            ShowProfileValue(
                labelText = "Last Name",
                value = profileClickData.value.lastName ,
                fieldClicked = {
                    profileClickData.value.profileClickState = ProfileClickState.LAST_NAME
                    dialogIsShowing.value = true
                },
                modifier = profileRowModifier)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "Gender",
                value = profileClickData.value.gender.toString(),
                fieldClicked = {
                    profileClickData.value.profileClickState = ProfileClickState.GENDER
                    dialogIsShowing.value = true
                },
                modifier = profileRowModifier)
            ShowProfileValue(
                labelText = "Birthday",
                value = profileClickData.value.birthDay.toString(),
                fieldClicked = {
                    profileClickData.value.profileClickState = ProfileClickState.BIRTHDAY
                    dialogIsShowing.value = true
                },
                modifier = profileRowModifier)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "Weight",
                value = "${profileClickData.value.weight}kg",
                fieldClicked = {
                    profileClickData.value.profileClickState = ProfileClickState.WEIGHT
                    dialogIsShowing.value = true
                },
                modifier = profileRowModifier)
            ShowProfileValue(
                labelText = "Height",
                value = "${profileClickData.value.height}cm",
                fieldClicked = {
                    profileClickData.value.profileClickState = ProfileClickState.HEIGHT
                    dialogIsShowing.value = true
                },
                modifier = profileRowModifier)
        }


    }
}

@Composable
fun ShowProfileValue(
    labelText: String,
    value: String,
    fieldClicked: () -> Unit,
    modifier: Modifier
)
{

    OutlinedTextField(
        value = value,
        onValueChange = { },
        label = {
            Text(
                text = labelText,
                color = Color(255, 255,255,191)
            )
        },
        modifier = modifier
            .padding(5.dp)
            .clickable { fieldClicked() },
        enabled = false,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
        fontSize = 16.sp)
    )
}

@Composable
fun AlertDialog(
    profileClickData :ProfileClickData,
    buttonClick: (ProfileClickData, Boolean) -> Unit
//todo ubaci composable za prikaz contenta dialoga
)
{
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value)
    {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                buttonClick.invoke(profileClickData, false)
           },
            title = {
                Text(
                    text = profileClickData.titleDialog,
                    color = MaterialTheme.colors.onBackground,
                    fontSize = 30.sp
                )
            },
            text = {
                   //todo content for inc and dec value
                   Row (
                       modifier = Modifier
                           .fillMaxWidth(),
                       horizontalArrangement = Arrangement.Center,
                       verticalAlignment = Alignment.CenterVertically
                   )
                   {
//                     TODO update with composable
                       Image (
                           painter = painterResource(id = R.drawable.ic_minus),
                           contentDescription = "Minus button",
                           modifier = Modifier
                               .height(40.dp)
                               .width(40.dp)
                               .clickable {
//                                   newProgress.value = newProgress.value - progressStep
//                                   todo update with composable
                               },
                           alignment = Alignment.Center
                       )
                       Spacer(modifier = Modifier.width(10.dp))
                       Text(
                           text = "awdaw",//newProgress.value.toString(),
                           color = MaterialTheme.colors.onBackground,
                           fontSize = 25.sp,
                           textAlign = TextAlign.Center
                       )
                       Spacer(modifier = Modifier.width(10.dp))
                       Image (
                           painter = painterResource(id = R.drawable.ic_plus),
                           contentDescription = "Plus button",
                           modifier = Modifier
                               .height(40.dp)
                               .width(40.dp)
                               .clickable {
//                                   newProgress.value = newProgress.value + progressStep
//                                   todo update with composable
                               },
                           alignment = Alignment.Center
                       )
                   }

                   },

            confirmButton = {

                TextButton(
                    onClick = {
                        buttonClick.invoke(profileClickData, true)
                        openDialog.value = false
                    }) {
                    Text(text = "Confirm", color = MaterialTheme.colors.onBackground)
                }

            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        buttonClick.invoke(profileClickData, false)
                    }) {
                    Text(text = "Dismiss", color = MaterialTheme.colors.onBackground)
                }
            },
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.background,
        )
    }
}