package com.example.jetpackcomposesvastara.presentation.composable.util

data class ProfileClickData(
    var profileClickState: ProfileClickState = ProfileClickState.STEPS,
    var gender: Gender = Gender.MALE,
    var stepsValue: Int = 1000,
    var birthDay: Date = Date(),
    var weight: Float = 113.5f,
    var height: Float = 187f,
    var firstName: String = "Jovan",
    var lastName: String = "Ristic",
    var titleDialog: String = "Title"
)
