// presentation/onboarding/OnboardingCoordinator.kt
package com.kcguran.menstrualtracking.presentation.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kcguran.menstrualtracking.presentation.navigation.Route

class OnboardingCoordinator(
    val viewModel: OnboardingViewModel,
    private val navController: NavController
) {
    val screenStateFlow = viewModel.stateFlow

    fun onNameChanged(name: String) {
        viewModel.updateName(name)
    }

    fun onSurnameChanged(surname: String) {
        viewModel.updateSurname(surname)
    }

    fun onAgeChanged(age: String) {
        viewModel.updateAge(age)
    }

    fun onHeightChanged(height: String) {
        viewModel.updateHeight(height)
    }

    fun onWeightChanged(weight: String) {
        viewModel.updateWeight(weight)
    }

    fun onSaveClicked() {
        viewModel.saveUserInfo()
    }

    fun navigateToMain() {
        navController.navigate(Route.MenstrualCycle.route) {
            popUpTo(Route.Onboarding.route) { inclusive = true }
        }
    }
}

@Composable
fun rememberOnboardingCoordinator(
    viewModel: OnboardingViewModel = hiltViewModel(),
    navController: NavController
): OnboardingCoordinator {
    return remember(viewModel, navController) {
        OnboardingCoordinator(
            viewModel = viewModel,
            navController = navController
        )
    }
}