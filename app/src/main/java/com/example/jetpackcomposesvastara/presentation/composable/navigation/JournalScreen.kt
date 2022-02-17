package com.example.jetpackcomposesvastara.presentation.composable.navigation

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.jetpackcomposesvastara.presentation.composable.util.MultiFabItem
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.presentation.composable.journals.JournalHydrationCard
import com.example.jetpackcomposesvastara.presentation.composable.journals.JournalWorkoutCard
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
    val listState = rememberLazyListState()
    var toState by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    var isHydrationOn by remember {
        viewModel.isHydrationOn
    }
    var isWorkoutOn by remember {
        viewModel.isWorkoutOn
    }

    val journalsList by viewModel.journalsListFiltered.observeAsState(mutableListOf())

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        color = MaterialTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            /**Filter Header*/
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
                    onClick = {
                        isHydrationOn = !isHydrationOn
                        viewModel.filterUpdate(isHydrationOn, isWorkoutOn)
                    })
                Spacer(
                    modifier = Modifier.width(10.dp)
                )
                SelectableButton(textBtn = "Workout",
                    isSelected = isWorkoutOn,
                    color = MaterialTheme.colors.onBackground,
                    selectedTextColor = MaterialTheme.colors.background,
                    onClick = {
                        isWorkoutOn = !isWorkoutOn
                        viewModel.filterUpdate(isHydrationOn, isWorkoutOn)
                    })
            }

            /**LIST*/
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                state = listState
            ) {
                items(journalsList.size) { index ->
                    if(journalsList[index].isHydration)
                    {
                        JournalHydrationCard(journalDataObject = journalsList[index]) {
                            val tempIdentifier = if(it.isHydration) "hydration" else "workout"
                            navController.navigate("journal_details/${it.uid}/$tempIdentifier")
                        }
                    }
                    else
                    {
                        JournalWorkoutCard(journalDataObject = journalsList[index]) {
                            val tempIdentifier = if(it.isHydration) "hydration" else "workout"
                            navController.navigate("journal_details/${it.uid}/$tempIdentifier")
                        }
                    }
                }
            }
        }

        /**FAB*/
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .wrapContentSize(Alignment.BottomEnd)
                .padding(15.dp)
        )
        {
            if(!listState.isScrollInProgress)
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
                    navController.navigate("journal_details/${-1}/${it.identifier}")
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
