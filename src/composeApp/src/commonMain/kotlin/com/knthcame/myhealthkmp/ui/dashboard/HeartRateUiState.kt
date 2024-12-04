package com.knthcame.myhealthkmp.ui.dashboard

import com.knthcame.myhealthkmp.data.diary.model.HeartRate

sealed class HeartRateUiState {
    data object Missing : HeartRateUiState()
    data class Available(
        val value: String,
        val timeStamp: String,
        val graphValues: List<HeartRate>,
    ) : HeartRateUiState()
}
