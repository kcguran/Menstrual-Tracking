// presentation/calendar/CalendarScreen.kt
package com.kcguran.menstrualtracking.presentation.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    state: CalendarState,
    actions: CalendarActions
) {
    val daysOfWeek = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Takvim",
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
                actions = {
                    // Silme butonunu buraya ekleyin
                    IconButton(onClick = actions.onShowDeleteConfirmDialog) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Tüm Regl Verilerini Sil",
                            tint = MaterialTheme.colorScheme.error
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
        ) {
            // Ay navigasyonu
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { actions.onMonthChanged(state.currentMonth.minusMonths(1)) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Önceki Ay",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = state.currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("tr"))),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    IconButton(
                        onClick = { actions.onMonthChanged(state.currentMonth.plusMonths(1)) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Sonraki Ay",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            // Hafta günleri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {

            }

            // Takvim grid'i - Daha büyük format
            Column(modifier = Modifier.fillMaxWidth()) {
                // Haftanın günleri başlıkları (Pazartesi=0, Pazar=6)
                Row(modifier = Modifier.fillMaxWidth()) {
                    val dayNames = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cts", "Paz")
                    dayNames.forEach { dayName ->
                        Text(
                            text = dayName,
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Takvim içeriği
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    // Ayın 1. gününün yılın kaçıncı ayı olduğunu belirle
                    val month = state.currentMonth.monthValue  // 1-12 arası
                    val year = state.currentMonth.year

                    // Ayın 1. gününün haftanın hangi günü olduğunu hesapla
                    // Calendar.MONDAY = 2, Calendar.TUESDAY = 3, ... Calendar.SUNDAY = 1
                    // 0=Pazartesi, 1=Salı, ... 6=Pazar olmasını istiyoruz
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month - 1, 1) // Java Calendar'da ay 0'dan başlar (Ocak=0)
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

                    // Pazartesi=0, Salı=1, ..., Pazar=6 dönüştürmesi
                    val firstDayOfWeek = when (dayOfWeek) {
                        Calendar.MONDAY -> 0
                        Calendar.TUESDAY -> 1
                        Calendar.WEDNESDAY -> 2
                        Calendar.THURSDAY -> 3
                        Calendar.FRIDAY -> 4
                        Calendar.SATURDAY -> 5
                        Calendar.SUNDAY -> 6
                        else -> 0 // Olası hata durumunda varsayılan değer
                    }

                    // Ayın kaç gün çektiğini hesapla
                    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()

                    // Ayın başındaki boşluklar
                    items(firstDayOfWeek) {
                        Box(modifier = Modifier.aspectRatio(1f))
                    }

                    // Ayın günleri
                    items(daysInMonth) { dayIndex ->
                        val day = dayIndex + 1 // 1'den başlamalı
                        val date = LocalDate.of(year, month, day)
                        val isSelectedDate = date == state.selectedDate
                        val color = getDayColor(date, state.menstrualCycles, state.predictedCycles, state.fertilityWindows)

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelectedDate) 3.dp else 0.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .clickable { actions.onDateSelected(date) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = day.toString(),
                                fontSize = 20.sp,
                                fontWeight = if (isSelectedDate) FontWeight.Bold else FontWeight.Normal,
                                color = if (color == Color.White) Color.Black else Color.White
                            )
                        }
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = state.selectedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("tr"))),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val dayStatus = getDayStatus(state.selectedDate, state.menstrualCycles, state.predictedCycles, state.fertilityWindows)
                    Text(
                        text = dayStatus,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        if (state.showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = actions.onHideDeleteConfirmDialog,
                title = {
                    Text(
                        "Tüm Regl Verilerini Sil",
                        color = MaterialTheme.colorScheme.error
                    )
                },
                text = {
                    Text(
                        "Tüm regl dönemi kayıtlarınız silinecek. Bu işlem geri alınamaz. Devam etmek istiyor musunuz?"
                    )
                },
                confirmButton = {
                    Button(
                        onClick = actions.onDeleteAllCycles,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Evet, Sil")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = actions.onHideDeleteConfirmDialog) {
                        Text("İptal")
                    }
                }
            )
        }
    }
}

// Takvim günlerinin renkleri
private fun getDayColor(
    date: LocalDate,
    actualCycles: List<MenstrualCycle>,
    predictedCycles: List<MenstrualCycle>,
    fertilityWindows: Map<Long, FertilityWindow>
): Color {
    // Gerçek regl dönemleri için pembe
    for (cycle in actualCycles) {
        if (cycle.endDate != null && date in cycle.startDate..cycle.endDate) {
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
        if (cycle.endDate != null && date in cycle.startDate..cycle.endDate) {
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

// Gün durumu metni
private fun getDayStatus(
    date: LocalDate,
    actualCycles: List<MenstrualCycle>,
    predictedCycles: List<MenstrualCycle>,
    fertilityWindows: Map<Long, FertilityWindow>
): String {
    // Gerçek regl dönemleri
    for (cycle in actualCycles) {
        if (cycle.endDate != null && date in cycle.startDate..cycle.endDate) {
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
        if (cycle.endDate != null && date in cycle.startDate..cycle.endDate) {
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