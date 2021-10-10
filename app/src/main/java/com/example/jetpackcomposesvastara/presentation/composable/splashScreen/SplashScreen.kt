package com.example.jetpackcomposesvastara.presentation.composable.splashScreen


import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.jetpackcomposesvastara.R
import com.example.jetpackcomposesvastara.presentation.composable.navigation.NavigationItem
import com.example.jetpackcomposesvastara.util.Constants.SPLASH_SCREEN_DURATION
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {
    val scale = remember {
        Animatable(0f)
    }
    val overshootInterpolator = remember {
        OvershootInterpolator(2f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.75f,
            animationSpec = tween(
                durationMillis = 750,
                easing = {
                    overshootInterpolator.getInterpolation(it)
                }
        ))
        delay(SPLASH_SCREEN_DURATION)
        navController.navigate(NavigationItem.Home.route) {
            popUpTo(NavigationItem.Splash.route)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image (
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Splash Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}