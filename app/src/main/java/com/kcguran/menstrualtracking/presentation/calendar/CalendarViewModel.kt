// presentation/calendar/CalendarViewModel.kt
package com.kcguran.menstrualtracking.presentation.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kcguran.menstrualtracking.domain.model.FertilityWindow
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.DeleteAllMenstrualCyclesUseCase
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.GetMenstrualCyclesUseCase
import com.kcguran.menstrualtracking.domain.usecase.menstrualcycle.PredictNextCycleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMenstrualCyclesUseCase: GetMenstrualCyclesUseCase,
    private val predictNextCycleUseCase: PredictNextCycleUseCase,
    private val deleteAllMenstrualCyclesUseCase: DeleteAllMenstrualCyclesUseCase
) : ViewModel() {

    private val _stateFlow: MutableStateFlow<CalendarState> = MutableStateFlow(CalendarState())
    val stateFlow: StateFlow<CalendarState> = _stateFlow.asStateFlow()

    init {
        loadMenstrualCycles()
    }

    private fun loadMenstrualCycles() {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isLoading = true) }

            try {
                getMenstrualCyclesUseCase().collectLatest { cycles ->
                    val predictedCycles = predictNextCycleUseCase(6)
                    val fertilityWindows = mutableMapOf<Long, FertilityWindow>()

                    // Tüm döngüler için doğurganlık pencerelerini hesapla
                    (cycles + predictedCycles).forEach { cycle ->
                        fertilityWindows[cycle.id] = predictNextCycleUseCase.calculateFertilityWindow(cycle)
                    }

                    _stateFlow.update { state ->
                        state.copy(
                            menstrualCycles = cycles,
                            predictedCycles = predictedCycles,
                            fertilityWindows = fertilityWindows,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Takvim verisi yüklenirken hata oluştu: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    // Belirli bir tarih aralığındaki döngüleri yükle
    fun loadCyclesByDateRange(startDate: LocalDate, endDate: LocalDate) {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isLoading = true) }

            try {
                // Use case'in date range metodunu kullan
                getMenstrualCyclesUseCase.getByDateRange(startDate, endDate).collectLatest { cycles ->
                    val predictedCycles = predictNextCycleUseCase(6)
                    val fertilityWindows = mutableMapOf<Long, FertilityWindow>()

                    (cycles + predictedCycles).forEach { cycle ->
                        fertilityWindows[cycle.id] = predictNextCycleUseCase.calculateFertilityWindow(cycle)
                    }

                    _stateFlow.update { state ->
                        state.copy(
                            menstrualCycles = cycles,
                            predictedCycles = predictedCycles,
                            fertilityWindows = fertilityWindows,
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Tarih aralığı verileri yüklenirken hata oluştu: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    // Güncel doğurganlık durumunu kontrol et
    fun checkCurrentFertilityStatus() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val fertilityWindow = predictNextCycleUseCase.checkFertilityForDate(today)

                _stateFlow.update { state ->
                    state.copy(
                        currentFertilityWindow = fertilityWindow
                    )
                }
            } catch (e: Exception) {
                // Hata durumunda loglama yapılabilir
            }
        }
    }

    fun selectDate(date: LocalDate) {
        _stateFlow.update { it.copy(selectedDate = date) }
    }

    fun changeMonth(month: YearMonth) {
        _stateFlow.update { it.copy(currentMonth = month) }

        // Ay değiştiğinde, o ayın tüm günlerini kapsayacak şekilde veri yükle
        val startOfMonth = month.atDay(1)
        val endOfMonth = month.atEndOfMonth()
        loadCyclesByDateRange(startOfMonth, endOfMonth)
    }

    fun showDeleteConfirmDialog() {
        _stateFlow.update { it.copy(showDeleteConfirmDialog = true) }
    }

    fun hideDeleteConfirmDialog() {
        _stateFlow.update { it.copy(showDeleteConfirmDialog = false) }
    }

    fun deleteAllCycles() {
        viewModelScope.launch {
            _stateFlow.update { it.copy(isLoading = true, showDeleteConfirmDialog = false) }

            try {
                deleteAllMenstrualCyclesUseCase()

                // Verileri yeniden yükle
                loadMenstrualCycles()
            } catch (e: Exception) {
                _stateFlow.update {
                    it.copy(
                        isLoading = false,
                        error = "Regl verileri silinirken hata oluştu: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}