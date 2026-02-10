package com.knthcame.myhealthkmp.ui.dashboard

import androidx.lifecycle.ViewModel
import com.knthcame.myhealthkmp.data.dashboard.repositories.DashboardRepository
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.ui.common.DateTimeStyle
import com.knthcame.myhealthkmp.ui.common.formatWithCurrentLocale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class DashboardViewModel(
    dashboardRepository: DashboardRepository,
    private val dateTimeRepository: DateTimeRepository,
    viewModelScope: CoroutineScope,
) : ViewModel(viewModelScope) {
    val heartRateState: StateFlow<HeartRateUiState> =
        combine(
            dashboardRepository.currentHeartRate,
            dashboardRepository.graphHeartRates,
        ) { current, graph ->
            if (current == null) HeartRateUiState.Missing
            else HeartRateUiState.Available(
                value = current.bpm,
                timeStamp = format(current.timeStamp),
                graphValues = graph.sortedBy { item -> item.timeStamp },
            )
        }.stateIn(viewModelScope, SharingStarted.Lazily, HeartRateUiState.Missing)

    private fun format(timeStamp: Instant): String {
        val localTimeStamp = timeStamp.toLocalDateTime(dateTimeRepository.systemTimeZone)
        if (localTimeStamp.date == dateTimeRepository.localNow.date) {
            return localTimeStamp.time.formatWithCurrentLocale()
        }

        return localTimeStamp.formatWithCurrentLocale(DateTimeStyle.Short)
    }
}