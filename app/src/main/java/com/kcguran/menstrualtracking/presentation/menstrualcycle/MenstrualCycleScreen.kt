package com.kcguran.menstrualtracking.presentation.menstrualcycle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kcguran.menstrualtracking.domain.model.FertilityWindow
import com.kcguran.menstrualtracking.domain.model.MenstrualCycle
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenstrualCycleScreen(
    state: MenstrualCycleState,
    actions: MenstrualCycleActions
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Regl Takip",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                actions = {
                    IconButton(onClick = actions.onNavigateToCalendar) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Takvim",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = actions.onNavigateToProfile) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profil",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Regl Ekle")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Ay navigasyonu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Önceki Ay",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("tr"))),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Sonraki Ay",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Hafta günleri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                for (dayOfWeek in DayOfWeek.entries) {
                    Text(
                        text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("tr")),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // presentation/menstrualcycle/MenstrualCycleScreen.kt (devamı)
            // Takvim grid'i
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Ay içindeki günlerin gösterimi
                val firstDayOfMonth = currentMonth.atDay(1)
                val lastDayOfMonth = currentMonth.atEndOfMonth()

                // Ayın ilk gününden önceki boşluklar
                val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
                items(firstDayOfWeek) {
                    Box(modifier = Modifier.aspectRatio(1f))
                }

                // Ayın günleri
                val days = (1..lastDayOfMonth.dayOfMonth).map { day ->
                    currentMonth.atDay(day)
                }

                items(days) { date ->
                    val isSelectedDate = date == state.selectedDate
                    val color = getDayColor(date, state.menstrualCycles, state.predictedCycles, state.fertilityWindows)

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelectedDate) 2.dp else 0.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            )
                            .clickable { actions.onDateSelected(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (color == Color.White) Color.Black else Color.White
                        )
                    }
                }
            }

            // Seçili gün bilgileri
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = state.selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("tr"))),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val dayStatus = getDayStatus(state.selectedDate, state.menstrualCycles, state.predictedCycles, state.fertilityWindows)
                    Text(
                        text = dayStatus,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (showAddDialog) {
            AddMenstrualCycleDialog(
                selectedDate = state.selectedDate,
                onDismiss = { showAddDialog = false },
                onConfirm = { date, periodLength ->
                    actions.onAddCycleClicked(date, periodLength)
                    showAddDialog = false
                }
            )
        }
    }
}


@Composable
private fun AddMenstrualCycleDialog(
    selectedDate: LocalDate,
    onDismiss: () -> Unit,
    onConfirm: (LocalDate, Int) -> Unit
) {
    var periodLength by remember { mutableStateOf(5) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Regl Dönemi Ekle") },
        text = {
            Column {
                Text(
                    text = "Başlangıç Tarihi: ${selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("tr")))}"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Regl Süresi (gün):")

                Slider(
                    value = periodLength.toFloat(),
                    onValueChange = { periodLength = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8
                )

                Text("$periodLength gün")
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(selectedDate, periodLength) }) {
                Text("Ekle")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

// Takvim günlerinin renklerini kadın temasına uygun şekilde güncelleyin
private fun getDayColor(
    date: LocalDate,
    actualCycles: List<MenstrualCycle>,
    predictedCycles: List<MenstrualCycle>,
    fertilityWindows: Map<Long, FertilityWindow>
): Color {
    // Gerçek regl dönemleri için pembe
    for (cycle in actualCycles) {
        if (date in cycle.startDate..cycle.endDate!!) {
            return Color(0xFFF48FB1) // Pink
        }

        // Doğurgan dönem için mor
        val fertilityWindow = fertilityWindows[cycle.id]
        if (fertilityWindow != null && date in fertilityWindow.startDate..fertilityWindow.endDate) {
            return Color(0xFFCE93D8) // Purple
        }
    }

    // Tahmin edilen regl dönemleri için açık pembe
    for (cycle in predictedCycles) {
        if (date in cycle.startDate..cycle.endDate!!) {
            return Color(0xFFF8BBD0) // LightPink
        }

        // Tahmin edilen doğurgan dönem için açık mor
        val fertilityWindow = fertilityWindows[cycle.id]
        if (fertilityWindow != null && date in fertilityWindow.startDate..fertilityWindow.endDate) {
            return Color(0xFFE1BEE7) // LightPurple
        }
    }

    // Normal günler için beyaz
    return Color.White
}

private fun getDayStatus(
    date: LocalDate,
    actualCycles: List<MenstrualCycle>,
    predictedCycles: List<MenstrualCycle>,
    fertilityWindows: Map<Long, FertilityWindow>
): String {
    // Gerçek regl dönemleri
    for (cycle in actualCycles) {
        if (date in cycle.startDate..cycle.endDate!!) {
            return "Regl dönemi (${cycle.startDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))} - ${cycle.endDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))})"
        }

        // Doğurgan dönem
        val fertilityWindow = fertilityWindows[cycle.id]
        if (fertilityWindow != null && date in fertilityWindow.startDate..fertilityWindow.endDate) {
            if (date == fertilityWindow.ovulationDate) {
                return "Yumurtlama günü"
            }
            return "Doğurgan dönem (${fertilityWindow.startDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))} - ${fertilityWindow.endDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))})"
        }
    }

    // Tahmin edilen regl dönemleri
    for (cycle in predictedCycles) {
        if (date in cycle.startDate..cycle.endDate!!) {
            return "Tahmini regl dönemi (${cycle.startDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))} - ${cycle.endDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))})"
        }

        // Tahmin edilen doğurgan dönem
        val fertilityWindow = fertilityWindows[cycle.id]
        if (fertilityWindow != null && date in fertilityWindow.startDate..fertilityWindow.endDate) {
            if (date == fertilityWindow.ovulationDate) {
                return "Tahmini yumurtlama günü"
            }
            return "Tahmini doğurgan dönem (${fertilityWindow.startDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))} - ${fertilityWindow.endDate.format(DateTimeFormatter.ofPattern("d MMM", Locale("tr")))})"
        }
    }

    return "Normal gün"
}