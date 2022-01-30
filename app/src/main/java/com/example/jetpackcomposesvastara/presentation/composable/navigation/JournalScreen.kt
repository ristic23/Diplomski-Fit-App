package com.example.jetpackcomposesvastara.presentation.composable.navigation

import android.widget.Space
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposesvastara.presentation.composable.util.MultiFabItem
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.presentation.viewModel.JournalViewModel
import kotlin.random.Random

//@Preview(showBackground = true)
//@Composable
//fun MoviesScreenPreview() {
//    JournalScreen()
//}

enum class MultiFabState {
    COLLAPSED, EXPANDED
}

@Composable
fun JournalScreen(
    navController: NavController,
    viewModel: JournalViewModel = hiltViewModel()
) {

    val items = listOf(
        MultiFabItem(
            "hydration",
            R.drawable.ic_hydration,
            "Hydration"
        ),
        MultiFabItem(
            "workout",
            R.drawable.ic_workout,
            "Workout"
        )
    )

    var toState by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    var isHydrationOn by remember { mutableStateOf(true) }
    var isWorkoutOn by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        color = MaterialTheme.colors.background
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(top = 8.dp)
        ) {
            SelectableButton(textBtn = "Hydration",
                isSelected = isHydrationOn,
                color = MaterialTheme.colors.onBackground,
                selectedTextColor = MaterialTheme.colors.background,
                onClick = { isHydrationOn = !isHydrationOn })
            Spacer(
                modifier = Modifier.width(10.dp)
            )
            SelectableButton(textBtn = "Workout",
                isSelected = isWorkoutOn,
                color = MaterialTheme.colors.onBackground,
                selectedTextColor = MaterialTheme.colors.background,
                onClick = { isWorkoutOn = !isWorkoutOn })
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .wrapContentSize(Alignment.BottomEnd)
                .padding(15.dp)
        )
        {
            MultiFloatingActionButton(
                modifier = Modifier
                    .width(56.dp)
                    .height(56.dp),
                items = items,
                toState = toState,
                navController = navController) {
                toState = it
            }
        }

    }
}

@Composable
fun SelectableButton(
    textBtn: String,
    isSelected: Boolean,
    color: Color,
    selectedTextColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.button
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(60.dp))
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(60.dp)
            )
            .background(
                color = if (isSelected) color else Color.Transparent,
                shape = RoundedCornerShape(60.dp)
            )
            .clickable {
                onClick()
            }
            .padding(10.dp)
    ) {
        Text(
            text = textBtn,
            style = textStyle,
            color = if(isSelected) selectedTextColor else color,
        )
    }
}


@Composable
fun MultiFloatingActionButton(
    modifier: Modifier,
    toState: MultiFabState,
    items: List<MultiFabItem>,
    navController: NavController,
    stateChanged: (MultiFabState) -> Unit
)
{
    val transition = updateTransition(targetState = toState, label = "")
    val rotation: Float by transition.animateFloat(label = ""){ state ->
        if (state == MultiFabState.EXPANDED)
            45f
        else
            0f
    }
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    )
    {
        if (transition.currentState == MultiFabState.EXPANDED) {
            items.forEach { item ->
                MiniFloatingActionButton(item) {
                    val randomID = Random.nextInt(2) - 1
                    navController.navigate("journal_details/${randomID}/${it.identifier}")
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                stateChanged(
                    if (transition.currentState == MultiFabState.EXPANDED)
                        MultiFabState.COLLAPSED
                    else
                        MultiFabState.EXPANDED
                )
            },
            modifier = modifier,
            backgroundColor = if (transition.currentState == MultiFabState.EXPANDED)
                MaterialTheme.colors.onSurface
            else
                MaterialTheme.colors.onBackground
        )
        {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Journal FAB",
                modifier = Modifier.rotate(rotation),
                tint = if (transition.currentState == MultiFabState.EXPANDED)
                    MaterialTheme.colors.onBackground
                else
                    MaterialTheme.colors.background
            )

        }
    }
}

@Composable
private fun MiniFloatingActionButton(
    item: MultiFabItem,
    onFabItemClicked: (MultiFabItem) -> Unit
)
{
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(50.dp)
            .wrapContentWidth()
            .clip(RoundedCornerShape(25.dp))
            .clickable {
                onFabItemClicked.invoke(item)
            }
    )
    {
        Text(
            text = item.label,
            color = MaterialTheme.colors.onBackground)
        Spacer(modifier = Modifier
            .width(10.dp)
            .fillMaxHeight())
        Box(
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_circle),
                contentDescription = null,
//                tint = MaterialTheme.colors.secondary,
                tint = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
            )
            Icon(
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp),
                painter = painterResource(id = item.icon),
                contentDescription = null,
                tint = MaterialTheme.colors.surface
            )
        }
    }



}
