package com.kcguran.menstrualtracking.presentation.navigation

sealed class Route(val route: String) {
    object MenstrualCycle : Route("menstrualCycle")
    object Calendar : Route("calendar")
    object Profile : Route("profile")
}