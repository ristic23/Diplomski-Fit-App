package com.example.jetpackcomposesvastara.presentation.composable.navigation

import android.graphics.Bitmap
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var toState by remember { mutableStateOf(MultiFabState.COLLAPSED) }

    val icon = Icons.Filled.Add

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
                    .width(50.dp)
                    .height(50.dp),
                fabIcon = icon,
                toState = toState) {
                toState = it
            }
        }

    }
}

@Composable
fun MultiFloatingActionButton(
    modifier: Modifier,
    fabIcon: ImageVector,
    toState: MultiFabState,
    stateChanged: (MultiFabState) -> Unit
) {
    val transition = updateTransition(targetState = toState, label = "")
    val rotation: Float by transition.animateFloat(label = ""){ state ->
        if (state == MultiFabState.EXPANDED)
            45f
        else
            0f
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        if (transition.currentState == MultiFabState.EXPANDED) {
            MiniFloatingActionButton(
                MultiFabItem(
                icon = painterResource(id = R.drawable.ic_hydration),
                    identifier = "",
                    label = "",
            )){}
            Spacer(modifier = Modifier.height(20.dp))
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
        )
        {
            Icon(
                imageVector = fabIcon,
                contentDescription = "Journal FAB",
                modifier = Modifier.rotate(rotation)
            )

        }
    }
}

@Composable
private fun MiniFloatingActionButton(
    item: MultiFabItem,
    onFabItemClicked: (MultiFabItem) -> Unit
) {
//    val circleBgColor = MaterialTheme.colors.primary
//    val icon = item.icon as Bitmap
//    val icon2 = icon as ImageBitmap
//    Canvas(
//        modifier = Modifier
//            .size(32.dp)
//            .clickable(
//                onClick = { onFabItemClicked(item) }
//            )
//    ) {
//        drawCircle(color = circleBgColor, radius = 40f)
//        drawImage(
//            image = icon2,
//            topLeft = Offset(
//                (this.center.x) - (item.icon.width / 2),
//                (this.center.y) - (item.icon.width / 2)
//            )
//        )
//    }
    Image(
        painter =  item.icon,
        contentDescription = null
    )
}
