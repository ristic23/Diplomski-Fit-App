package com.example.jetpackcomposesvastara.presentation.composable.navigation

import com.example.jetpackcomposesvastara.R

sealed class NavigationItem(
    var route: String,
    var icon: Int = R.drawable.ic_home,
    var title: String = "",
    var isSelected :Boolean = false)


{
    object Home : NavigationItem("home", R.drawable.ic_home, "Home", false)
    object Goals : NavigationItem("Goals", R.drawable.ic_goal, "Goals", false)
    object Journal : NavigationItem("Journal", R.drawable.ic_book, "Journal", false)
    object Profile : NavigationItem("profile", R.drawable.ic_profile, "Profile", false)

    object Splash : NavigationItem(route = "splash", title= "Splash")
}
