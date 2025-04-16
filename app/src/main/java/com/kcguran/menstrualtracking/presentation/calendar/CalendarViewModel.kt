// presentation/calendar/CalendarViewModel.kt
package com.kcguran.menstrualtracking.presentation.calendar

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kcguran.menstrualtracking.domain.model.FertilityWindow
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
    private val predictNextCycleUseCase: PredictNextCycleUseCase
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

                    // Calculate fertility windows for actual and predicted cycles
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

    fun selectDate(date: LocalDate) {
        _stateFlow.update { it.copy(selectedDate = date) }
    }

    fun changeMonth(month: YearMonth) {
        _stateFlow.update { it.copy(currentMonth = month) }
    }
}