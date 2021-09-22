package com.example.jetpackcomposesvastara.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jetpackcomposesvastara.R

//ovaj sluzi samo za preview
@Composable
fun BottomNavigationBar() {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Music,
        NavigationItem.Movies,
        NavigationItem.Profile
    )
    BottomNavigation(
        backgroundColor = colorResource(id = R.color.teal_200),
        contentColor = Color.White
    ) {
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    BottomNavBarIcon(iconId = item.icon,
                        isSelected = item.isSelected,
                        text = item.title) },
                label = { Text(text = "") },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(0.4f),
                alwaysShowLabel = false,
                selected = item.isSelected,
                onClick = {
                    //u main actvitiy napravljen
                }
            )
        }
    }
}

@Composable
fun BottomNavBarIcon(
    isSelected :Boolean = true,
    iconId : Int = R.drawable.ic_home,
    text : String = "Home"
)
{
    Surface(
//        border = BorderStroke(1.dp, if(isSelected) Color.White else Color.Transparent),
        shape = CircleShape,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()

    ) {
        Surface(modifier = Modifier.padding(4.dp),
            color = Color.Transparent) {

            Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically)
            {
                Image(
                    painter = painterResource(iconId),
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp)
                )
                Text(text = if(isSelected) text else "",
                    color = Color.White,
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview()
{
    BottomNavigationBar()
//    BottomNavBarIcon()
}
