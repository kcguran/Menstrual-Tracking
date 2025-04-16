package com.kcguran.menstrualtracking.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kcguran.menstrualtracking.presentation.calendar.CalendarRoute
import com.kcguran.menstrualtracking.presentation.menstrualcycle.MenstrualCycleRoute
import com.kcguran.menstrualtracking.presentation.profile.ProfileRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val items = listOf(
        Triple(Route.MenstrualCycle, "Regl Takip", Icons.Default.Settings),
        Triple(Route.Calendar, "Takvim", Icons.Default.DateRange),
        Triple(Route.Profile, "Profil", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { (route, title, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = title) },
                        label = { Text(title) },
                        selected = currentDestination?.hierarchy?.any { it.route == route.route } == true,
                        onClick = {
                            navController.navigate(route.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.MenstrualCycle.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Route.MenstrualCycle.route) {
                MenstrualCycleRoute(navController = navController)
            }
            /*
            composable(Route.Calendar.route) {
                CalendarRoute(navController = navController)
            }
            composable(Route.Profile.route) {
                ProfileRoute(navController = navController)
            }

             */
        }
    }
}