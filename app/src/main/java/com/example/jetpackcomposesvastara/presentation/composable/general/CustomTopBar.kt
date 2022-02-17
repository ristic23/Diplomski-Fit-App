package com.example.jetpackcomposesvastara.presentation.composable.general

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jetpackcomposesvastara.R

@Composable
fun DetailsTopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    text: String,
    deleteClicked: () -> Unit,
    doneClicked: () -> Unit,
    uid: Int
) {
    val iconPadding = 8.dp
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .padding(iconPadding)
                .clip(CircleShape)
                .clickable {
                    navController.popBackStack()
                }
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .align(Alignment.CenterVertically),
            fontSize = 24.sp
        )

        Row(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if(uid != -1)
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = colorResource(id = R.color.delete_btn_color),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(iconPadding)
                        .clip(CircleShape)
                        .clickable {
                            deleteClicked.invoke()
                        }
                )
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                tint = colorResource(id = R.color.done_btn_color),
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .padding(iconPadding)
                    .clip(CircleShape)
                    .clickable {
                        doneClicked.invoke()
                    }
            )
        }
    }
}

@Composable
fun CustomOutlinedTextField(
    inputHint: String,
    value: String,
    onValueChange: (String) -> Unit,
    fieldClicked: () -> Unit,
    enabled: Boolean,
    keyboardType: KeyboardType
)
{
    var inputValue by remember(key1 = value) { mutableStateOf(value) }
//    var isUpdateNeeded by remember { mutableStateOf(true) }
//
//    if(value.isNotEmpty() && isUpdateNeeded && inputValue.isEmpty()) {
//        inputValue = value
//        isUpdateNeeded = false
//    }

    val focusedColorValue = colorResource(id = R.color.TextWhite)
    val notFocusedColorValue = colorResource(id = R.color.LightGray)
    var focusedColor by remember {
        mutableStateOf(notFocusedColorValue)
    }

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
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
            value = inputValue,
            onValueChange = {
                inputValue = it
                onValueChange.invoke(it)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = keyboardType),
            label = {
                Text(
                    text = inputHint,
                    color = focusedColor
                )
            },
            enabled = enabled,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = focusedColorValue,
                unfocusedBorderColor = notFocusedColorValue
            ),
            keyboardActions = KeyboardActions(
                onNext = {focusManager.moveFocus(FocusDirection.Down)})
        )
    }
}

@Composable
fun RowWithDescAndAction(
    descAction: String,
    value: String,
    fieldClicked: () -> Unit,
    suffix: String = ""
)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(8.dp)
            .clickable {
                fieldClicked.invoke()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        Text(
            text = descAction,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground
        )

        Text(
            text = "$value $suffix",
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground
        )
    }
}

