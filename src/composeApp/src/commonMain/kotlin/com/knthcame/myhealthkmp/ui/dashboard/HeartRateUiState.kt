package com.knthcame.myhealthkmp.ui.dashboard

sealed class HeartRateUiState {
    data object Missing : HeartRateUiState()
    data class Available(
        val value: String,
        val timeStamp: String,
    ) : HeartRateUiState()
}
