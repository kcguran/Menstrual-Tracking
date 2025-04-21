package com.kcguran.menstrualtracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.kcguran.menstrualtracking.domain.enum.StartDestination
import com.kcguran.menstrualtracking.domain.repository.UserProfileRepository
import com.kcguran.menstrualtracking.domain.usecase.userprofile.GetUserProfileUseCase
import com.kcguran.menstrualtracking.presentation.navigation.AppNavigation
import com.kcguran.menstrualtracking.presentation.theme.MenstrualTrackingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getUserProfileUseCase: GetUserProfileUseCase

    @Inject
    lateinit var userProfileRepository: UserProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // İlk başta, onboarding gösterilip gösterilmeyeceğini belirle
        var startDestination: StartDestination? by mutableStateOf(null)

        lifecycleScope.launch {
            try {
                val userProfile = getUserProfileUseCase().first()
                startDestination = if (userProfile.isFirstLogin) {
                    StartDestination.ONBOARDING
                } else {
                    StartDestination.MAIN
                }
            } catch (e: Exception) {
                // İlk kez çalıştığında olabilir
                startDestination = StartDestination.ONBOARDING
            }
        }

        setContent {
            MenstrualTrackingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // startDestination belirlenene kadar bekle
                    startDestination?.let { destination ->
                        AppNavigation(startDestination = destination)
                    }
                }
            }
        }
    }
}