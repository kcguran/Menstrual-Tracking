package com.kcguran.menstrualtracking.presentation.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.LocalDate

class ProfileCoordinator(
    val viewModel: ProfileViewModel,
    private val navController: NavController
) {
    val screenStateFlow = viewModel.stateFlow

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateToSettings() {
        // Eğer ileride bir ayarlar sayfası eklerseniz buraya yönlendirme ekleyebilirsiniz
        // Şu an için kullanılmıyor
        // navController.navigate("settings")
    }
}

@Composable
fun rememberProfileCoordinator(
    viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
): ProfileCoordinator {
    return remember(viewModel, navController) {
        ProfileCoordinator(
            viewModel = viewModel,
            navController = navController
        )
    }
}