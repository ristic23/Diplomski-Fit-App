package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.presentation.composable.util.Gender
import com.example.jetpackcomposesvastara.presentation.composable.util.ProfileClickData
import com.example.jetpackcomposesvastara.presentation.composable.util.ProfileClickState

//val context = LocalContext.current

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}

@ExperimentalComposeUiApi
@Composable
fun ProfileScreen() {
    var expanded by remember {
        mutableStateOf(false)
    }
    val profileClickData by remember {
        mutableStateOf(ProfileClickData())
    }
    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    val suggestions = listOf(Gender.Male, Gender.Female)
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    )
    {

        val profileRowModifier = Modifier
            .padding(5.dp)
            .weight(1f)

        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "Steps",
                value = profileClickData.stepsValue.toString(),
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                updateValue = {
                    profileClickData.stepsValue = it.toInt()
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "First Name",
                value = profileClickData.firstName,
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                updateValue = {
                    profileClickData.firstName = it
                }
            )
            ShowProfileValue(
                labelText = "Last Name",
                value = profileClickData.lastName ,
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                updateValue = {
                    profileClickData.lastName = it
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            Column(modifier = profileRowModifier)
            {
                ShowProfileValueGender(
                    labelText = "Gender",
                    value = profileClickData.gender.toString(),
                    fieldClicked = {
                        profileClickData.profileClickState = ProfileClickState.GENDER
                        expanded = !expanded
                    },
                    modifier = Modifier,
                    enabled = false,
                    keyboardType = KeyboardType.Text,
                    textFieldSize = {
                        textFieldSize = it
                    },
                    updateValue = {
                    },
                    icon = icon
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){textFieldSize.width.toDp()})
                ) {
                    suggestions.forEach { gender ->
                        DropdownMenuItem(onClick = {
                            profileClickData.gender = gender
                            expanded = false
                        }) {
                            Text(
                                text = gender.toString(),
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            ShowProfileValue(
                labelText = "Birthday",
                value = profileClickData.birthDay.toString(),
                fieldClicked = {
                    profileClickData.profileClickState = ProfileClickState.BIRTHDAY
//                    dialogIsShowing.value = true
                },
                modifier = profileRowModifier,
                enabled = false,
                keyboardType = KeyboardType.Text,
                updateValue = {
                }
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "Weight",
                value = "${profileClickData.weight}",
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                suffix = "kg",
                updateValue = {
                    profileClickData.weight = it.toInt()
                }
            )
            ShowProfileValue(
                labelText = "Height",
                value = "${profileClickData.height}",
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                suffix = "cm",
                updateValue = {
                    profileClickData.height = it.toInt()
                }
            )
        }



    }

}

//todo next date picker

@ExperimentalComposeUiApi
@Composable
fun ShowProfileValue(
    labelText: String,
    value: String,
    fieldClicked: () -> Unit,
    textFieldSize: ((Size) -> Unit)? = null,
    modifier: Modifier,
    enabled: Boolean,
    keyboardType: KeyboardType,
    suffix: String = "",
    updateValue: (String) -> Unit
)
{
    var newValue by remember {
        mutableStateOf(value)
    }
    val focusedColorValue = Color(255, 255,255,191)
    val notFocusedColorValue = Color(255, 255,255,128)
    var focusedColor by remember {
        mutableStateOf(focusedColorValue)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = "$newValue$suffix",
        onValueChange = {
            if(enabled)
            {
                newValue = it
                updateValue.invoke(newValue)
            }
        },
        label = {
            Text(
                text = labelText,
                color = focusedColor
            )
        },
        modifier = modifier
            .padding(5.dp)
            .onGloballyPositioned { coordinates ->
                //This value is used to assign to the DropDown the same width
                textFieldSize?.invoke(coordinates.size.toSize())
            }
            .clickable {
                if (!enabled)
                    fieldClicked()
            }
            .onFocusChanged {
                focusedColor = if (it.isFocused)
                    focusedColorValue
                else
                    notFocusedColorValue
            },
        enabled = enabled,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = focusedColorValue,
            unfocusedBorderColor = notFocusedColorValue
        ),
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()
        })
    )
}

@Composable
fun ShowProfileValueGender(
    labelText: String,
    value: String,
    fieldClicked: () -> Unit,
    textFieldSize: ((Size) -> Unit)? = null,
    modifier: Modifier,
    enabled: Boolean,
    keyboardType: KeyboardType,
    suffix: String = "",
    updateValue: (String) -> Unit,
    icon: ImageVector
)
{
    var newValue by remember {
        mutableStateOf(value)
    }
    val focusedColorValue = Color(255, 255,255,191)
    val notFocusedColorValue = Color(255, 255,255,128)
    var focusedColor by remember {
        mutableStateOf(focusedColorValue)
    }

    OutlinedTextField(
        value = "$newValue$suffix",
        onValueChange = {
                if(enabled)
                {
                    newValue = it
                    updateValue.invoke(newValue)
                }
        },
        label = {
            Text(
                text = labelText,
                color = focusedColor
            )
        },
        modifier = modifier
            .padding(5.dp)
            .onGloballyPositioned { coordinates ->
                //This value is used to assign to the DropDown the same width
                textFieldSize?.invoke(coordinates.size.toSize())
            }
            .clickable {
                if (!enabled)
                    fieldClicked()
            }
            .onFocusChanged {
                focusedColor = if (it.isFocused)
                    focusedColorValue
                else
                    notFocusedColorValue
            },
        enabled = enabled,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        trailingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "contentDescription",
                tint = focusedColor
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = focusedColorValue,
            unfocusedBorderColor = notFocusedColorValue
        ),
        singleLine = true
    )
}

@Composable
fun DialogWithIncrementOption(
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