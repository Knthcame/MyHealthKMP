package com.knthcame.myhealthkmp.ui.dashboard

import androidx.lifecycle.ViewModel
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.repositories.DiaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.toLocalDateTime

class DashboardViewModel(
    diaryRepository: DiaryRepository,
    dateTimeRepository: DateTimeRepository,
    viewModelScope: CoroutineScope,
) : ViewModel(viewModelScope) {
    val heartRateState: StateFlow<HeartRateUiState> = diaryRepository.currentHeartRate
        .map { heartRate ->
            if (heartRate == null)
                return@map HeartRateUiState.Missing

            HeartRateUiState.Available(
                value = heartRate.bpm.toString(),
                timeStamp = heartRate.timeStamp.toLocalDateTime(dateTimeRepository.systemTimeZone)
                    .toString(),
            )

        }
        .stateIn(viewModelScope, SharingStarted.Lazily, HeartRateUiState.Missing)
}