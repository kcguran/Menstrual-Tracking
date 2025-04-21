package com.kcguran.menstrualtracking.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcguran.menstrualtracking.domain.enum.CycleStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

// presentation/profile/ProfileScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    actions: ProfileActions
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profil",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = actions.onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Geri",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Durum kartı
            CycleStatusCard(state)

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (state.error != null) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Kişisel Bilgiler Kartı
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Kişisel Bilgiler",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // İsim Soyisim
                    val fullName = buildString {
                        append(state.userProfile.name)
                        if (state.userProfile.surname.isNotEmpty()) {
                            append(" ")
                            append(state.userProfile.surname)
                        }
                    }

                    if (fullName.isNotEmpty()) {
                        ProfileInfoRow(label = "İsim", value = fullName)
                    }

                    // Yaş
                    if (state.userProfile.age > 0) {
                        ProfileInfoRow(label = "Yaş", value = "${state.userProfile.age}")
                    }

                    // Boy
                    if (state.userProfile.height > 0) {
                        ProfileInfoRow(label = "Boy", value = "${state.userProfile.height} cm")
                    }

                    // Kilo
                    if (state.userProfile.weight > 0) {
                        ProfileInfoRow(label = "Kilo", value = "${state.userProfile.weight} kg")
                    }
                }
            }

            // Regl Döngüsü Bilgileri Kartı
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Regl Döngüsü Bilgileri",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Son regl tarihi
                    state.userProfile.lastPeriodStartDate?.let { date ->
                        ProfileInfoRow(
                            label = "Son Regl Tarihi",
                            value = date.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("tr")))
                        )
                    }

                    // Ortalama Döngü Uzunluğu
                    ProfileInfoRow(
                        label = "Ortalama Döngü Uzunluğu",
                        value = "${state.userProfile.averageCycleLength} gün"
                    )

                    // Ortalama Regl Süresi
                    ProfileInfoRow(
                        label = "Ortalama Regl Süresi",
                        value = "${state.userProfile.averagePeriodLength} gün"
                    )
                }
            }

            // Yaygın Semptomlar Kartı
            if (state.userProfile.commonSymptoms.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Yaygın Semptomlarım",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        state.userProfile.commonSymptoms.forEach { symptom ->
                            Text(
                                text = "• $symptom",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Bildirim Ayarları Kartı
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Bildirimler",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (state.userProfile.notificationsEnabled) "Açık" else "Kapalı",
                        color = if (state.userProfile.notificationsEnabled)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.error
                    )
                }
            }

            // Ayarlar butonunu ekleyelim (İsteğe bağlı)
            Button(
                onClick = actions.onNavigateToSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Ayarlar",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Profil Ayarlarını Düzenle", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CycleStatusCard(state: ProfileState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (state.currentCycleStatus) {
                CycleStatus.InPeriod -> Color(0xFFF8BBD0)  // Light Pink
                CycleStatus.Fertile -> Color(0xFFE1BEE7)   // Light Purple
                CycleStatus.NotInPeriod -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = when (state.currentCycleStatus) {
                    CycleStatus.InPeriod -> "Regl Dönemindeyim"
                    CycleStatus.Fertile -> "Doğurgan Dönemdeyim"
                    CycleStatus.NotInPeriod -> "Normal Dönemdeyim"
                },
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            // Döngü durumuna göre ek bilgi
            when (state.currentCycleStatus) {
                CycleStatus.InPeriod -> {
                    state.daysLeftInCurrentPeriod?.let {
                        Text("Reglin bitmesine $it gün kaldı")
                    }
                }
                CycleStatus.Fertile -> {
                    Text("Doğurgan dönemdesin, dikkatli ol!")
                }
                CycleStatus.NotInPeriod -> {
                    state.daysUntilNextPeriod?.let {
                        Text("Bir sonraki regl dönemine $it gün kaldı")
                    }
                }
            }

            // Olası semptomlar
            if (state.possibleSymptoms.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Olası Semptomlar:",
                    fontWeight = FontWeight.Bold
                )
                state.possibleSymptoms.take(3).forEach { symptom ->
                    Text("• $symptom")
                }
                if (state.possibleSymptoms.size > 3) {
                    Text("• ve ${state.possibleSymptoms.size - 3} adet daha...")
                }
            }
        }
    }
}