package com.knthcame.myhealthkmp.data.dashboard.repositories

import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    val currentHeartRate: Flow<HeartRate?>
    val graphHeartRates: Flow<List<HeartRate>>
}
