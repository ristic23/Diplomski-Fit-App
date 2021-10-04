package com.example.jetpackcomposesvastara.composable.navigation

import com.example.jetpackcomposesvastara.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String, var isSelected :Boolean) {
    object Home : NavigationItem("home", R.drawable.ic_home, "Home", false)
    object Music : NavigationItem("music", R.drawable.ic_music, "Music", false)
    object Movies : NavigationItem("movies", R.drawable.ic_movie, "Movies", false)
    object Profile : NavigationItem("profile", R.drawable.ic_profile, "Profile", false)
}
