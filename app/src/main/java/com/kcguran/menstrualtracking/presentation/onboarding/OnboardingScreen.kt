// presentation/onboarding/OnboardingScreen.kt
package com.kcguran.menstrualtracking.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    state: OnboardingState,
    actions: OnboardingActions
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hoş Geldiniz!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Regl takip uygulamasını kişiselleştirmek için lütfen bilgilerinizi girin.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Adı
            OutlinedTextField(
                value = state.name,
                onValueChange = actions.onNameChanged,
                label = { Text("Adınız*") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Soyadı
            OutlinedTextField(
                value = state.surname,
                onValueChange = actions.onSurnameChanged,
                label = { Text("Soyadınız") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Yaş
            OutlinedTextField(
                value = state.age,
                onValueChange = actions.onAgeChanged,
                label = { Text("Yaşınız*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Boy
            OutlinedTextField(
                value = state.height,
                onValueChange = actions.onHeightChanged,
                label = { Text("Boyunuz (cm)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Kilo
            OutlinedTextField(
                value = state.weight,
                onValueChange = actions.onWeightChanged,
                label = { Text("Kilonuz (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = actions.onSaveClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Kaydet ve Devam Et", fontSize = 16.sp)
                }
            }

            Text(
                text = "* ile işaretli alanlar zorunludur",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}