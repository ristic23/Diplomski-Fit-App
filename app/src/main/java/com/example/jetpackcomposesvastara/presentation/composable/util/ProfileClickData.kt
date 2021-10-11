package com.example.jetpackcomposesvastara.presentation.composable.util

data class ProfileClickData(
    var profileClickState: ProfileClickState = ProfileClickState.INIT,
    var gender: Gender = Gender.Male,
    var stepsValue: Int = 1000,
    var birthDay: Date = Date(),
    var weight: Int = 113,
    var height: Int = 187,
    var firstName: String = "Jovan",
    var lastName: String = "Ristic",
    var titleDialog: String = "Title"
)
