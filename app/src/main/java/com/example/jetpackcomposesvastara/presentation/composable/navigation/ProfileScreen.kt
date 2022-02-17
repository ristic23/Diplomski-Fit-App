package com.example.jetpackcomposesvastara.presentation.composable.navigation

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.jetpackcomposesvastara.presentation.viewModel.ProfileViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.common.Gender
import com.example.jetpackcomposesvastara.presentation.composable.util.Date
import java.util.*
import java.util.Calendar.*

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

    var textFieldSize by remember {
        mutableStateOf(Size.Zero)
    }
    val suggestions = listOf(Gender.Male, Gender.Female)
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    val viewModel: ProfileViewModel = hiltViewModel()
    val currContext = LocalContext.current
    val firstName by viewModel.firstNameLiveData.observeAsState("")
    val lastName by viewModel.lastNameLiveData.observeAsState("")
    val gender by viewModel.genderLiveData.observeAsState(Gender.Male)
    val birthday by viewModel.birthdayLiveData.observeAsState(Date(
        getInstance().get(DAY_OF_MONTH),
        getInstance().get(MONTH) + 1,
        getInstance().get(YEAR),
    ).toString())
    val weight by viewModel.weightLiveData.observeAsState(0)
    val height by viewModel.heightLiveData.observeAsState(0)

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
                labelText = "First Name",
                value = firstName,
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Text,
                updateValue = {
//                    firstName = it
                },
                notFocusedListener = {
                    viewModel.setNewFirstName(it)
                }
            )
            ShowProfileValue(
                labelText = "Last Name",
                value = lastName,
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Text,
                updateValue = {
//                    lastName = it
                },
                notFocusedListener = {
                    viewModel.setNewLastName(it)
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
                    value = gender.toString(),
                    fieldClicked = {
                        expanded = !expanded
                    },
                    modifier = Modifier,
                    keyboardType = KeyboardType.Text,
                    textFieldSize = {
                        textFieldSize = it
                    },
                    icon = icon
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current){textFieldSize.width.toDp()})
                ) {
                    suggestions.forEach { newGender ->
                        DropdownMenuItem(onClick = {
                            viewModel.setNewGender(newGender)
                            expanded = false
                        }) {
                            Text(
                                text = newGender.toString(),
                                color = MaterialTheme.colors.onBackground,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            ShowProfileValueBirthday(
                labelText = "Birthday",
                value = birthday.toString(),
                fieldClicked = {
                    viewModel.selectDateTime(currContext) {
                        viewModel.setNewBirthday(it)
                    }
                },
                modifier = profileRowModifier
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth())
        {
            ShowProfileValue(
                labelText = "Weight",
                value = weight.toString(),
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                suffix = "kg",
                updateValue = {
//                    viewModel.we = if(it.isEmpty()) 0 else it.removeSuffix("kg").toInt()
                },
                notFocusedListener = {
                    viewModel.setNewWeight(if(it.isEmpty()) 0 else it.removeSuffix("kg").toInt())
                }
            )
            ShowProfileValue(
                labelText = "Height",
                value = height.toString(),
                fieldClicked = {
                },
                modifier = profileRowModifier,
                enabled = true,
                keyboardType = KeyboardType.Number,
                suffix = "cm",
                updateValue = {
//                    viewModel.height = if(it.isEmpty()) 0 else it.removeSuffix("cm").toInt()
                },
                notFocusedListener = {
                    viewModel.setNewHeight(if(it.isEmpty()) 0 else it.removeSuffix("cm").toInt())
                }
            )
        }



    }

}

@ExperimentalComposeUiApi
@Composable
fun ShowProfileValue(
    labelText: String,
    value: String,
    fieldClicked: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    keyboardType: KeyboardType,
    suffix: String = "",
    updateValue: (String) -> Unit,
    notFocusedListener: (String) -> Unit
)
{
//    var newValue by remember {
//        mutableStateOf(value)
//    }
    var newValue by remember(key1 = value) {
        mutableStateOf(value)
    }

    val focusedColorValue = Color(255, 255,255,191)
    val notFocusedColorValue = Color(255, 255,255,128)
    var focusedColor by remember {
        mutableStateOf(focusedColorValue)
    }

    var hasFocus by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = "$newValue$suffix",
        onValueChange = {
            if(enabled)
            {
                if(suffix.isNotEmpty())
                {
                    if(it[it.length - 1].isDigit())
                    {
                        newValue = if(newValue.isNotEmpty() && newValue == "0") "" else newValue
                        newValue = "$newValue${it[it.length - 1]}"
                        updateValue.invoke("$newValue$suffix")
                    }
                    else
                    {
                        if(!it.endsWith(suffix))
                        {
                            newValue = if(newValue.dropLast(1).isEmpty()) "0" else newValue.dropLast(1)
                            updateValue.invoke("$newValue$suffix")
                        }
                        else
                        {
                            newValue = if(it.removeSuffix(suffix).isEmpty()) "0" else it.removeSuffix(suffix)
                            updateValue.invoke("$newValue$suffix")
                        }
                    }
                }
                else
                {

                    newValue = if(keyboardType == KeyboardType.Number && it.isNotEmpty() && !it[it.length - 1].isDigit())
                        it.dropLast(1)
                    else {
                        if (it.isNotEmpty() && it[0].isDigit() && it[0].digitToInt() == 0)
                            it.removeRange(0, 1)
                        else
                            it
                    }
                    updateValue.invoke(newValue)
                }

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
            .clickable {
                if (!enabled)
                    fieldClicked()
            }
            .onFocusChanged {
                focusedColor = if (it.isFocused) {
                    hasFocus = true
                    focusedColorValue
                } else
                    notFocusedColorValue
                if (!it.isFocused && hasFocus) {
                    hasFocus = false
                    notFocusedListener.invoke(newValue)
                }
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

@ExperimentalComposeUiApi
@Composable
fun ShowProfileValueBirthday(
    labelText: String,
    value: String,
    fieldClicked: () -> Unit,
    modifier: Modifier
)
{
    var newValue by remember {
        mutableStateOf(value)
    }
    newValue = value
    val focusedColorValue = Color(255, 255,255,191)
    val notFocusedColorValue = Color(255, 255,255,128)
    var focusedColor by remember {
        mutableStateOf(focusedColorValue)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = newValue,
        onValueChange = { },
        label = {
            Text(
                text = labelText,
                color = focusedColor
            )
        },
        modifier = modifier
            .padding(5.dp)
            .clickable {
                fieldClicked()
            }
            .onFocusChanged {
                focusedColor = if (it.isFocused)
                    focusedColorValue
                else
                    notFocusedColorValue
            },
        enabled = false,
        textStyle = TextStyle(
            color = MaterialTheme.colors.onBackground,
            fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
    keyboardType: KeyboardType,
    icon: ImageVector
)
{

    val focusedColorValue = Color(255, 255,255,191)
    val notFocusedColorValue = Color(255, 255,255,128)
    var focusedColor by remember {
        mutableStateOf(focusedColorValue)
    }

    OutlinedTextField(
        value = value,
        onValueChange = { },
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
                fieldClicked()
            }
            .onFocusChanged {
                focusedColor = if (it.isFocused)
                    focusedColorValue
                else
                    notFocusedColorValue
            },
        enabled = false,
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

