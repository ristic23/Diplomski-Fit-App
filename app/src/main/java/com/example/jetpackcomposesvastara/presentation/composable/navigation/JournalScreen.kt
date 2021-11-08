package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposesvastara.presentation.composable.util.MultiFabItem
import com.example.jetpackcomposesvastara.R

@Preview(showBackground = true)
@Composable
fun MoviesScreenPreview() {
    JournalScreen()
}

enum class MultiFabState {
    COLLAPSED, EXPANDED
}

@Composable
fun JournalScreen() {

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

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp)
    ) {
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
                toState = toState) {
                toState = it
            }
        }

    }
}

@Composable
fun MultiFloatingActionButton(
    modifier: Modifier,
    toState: MultiFabState,
    items: List<MultiFabItem>,
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
            modifier = modifier
//            , backgroundColor = if (transition.currentState == MultiFabState.EXPANDED)
//                MaterialTheme.colors.background
//            else
//                MaterialTheme.colors.secondary
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
                tint = MaterialTheme.colors.secondary,
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
