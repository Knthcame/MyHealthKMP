package com.knthcame.myhealthkmp.ui.dashboard

import androidx.lifecycle.ViewModel
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.repositories.DiaryRepository
import com.knthcame.myhealthkmp.ui.common.DateTimeStyle
import com.knthcame.myhealthkmp.ui.common.formatWithCurrentLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

class DashboardViewModel(
    diaryRepository: DiaryRepository,
    private val dateTimeRepository: DateTimeRepository,
    viewModelScope: CoroutineScope,
) : ViewModel(viewModelScope) {
    val heartRateState: StateFlow<HeartRateUiState> = diaryRepository.currentHeartRate
        .map { heartRate ->
            if (heartRate == null)
                return@map HeartRateUiState.Missing

            HeartRateUiState.Available(
                value = heartRate.bpm.toString(),
                timeStamp = format(heartRate.timeStamp),
            )

        }
        .stateIn(viewModelScope, SharingStarted.Lazily, HeartRateUiState.Missing)

    private fun format(timeStamp: Instant): String {
        val localTimeStamp = timeStamp.toLocalDateTime(dateTimeRepository.systemTimeZone)
        if (localTimeStamp.date == dateTimeRepository.localNow.date) {
            return localTimeStamp.time.formatWithCurrentLocale()
        }

        return localTimeStamp.formatWithCurrentLocale(DateTimeStyle.Short)
    }
}