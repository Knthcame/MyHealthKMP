package com.knthcame.myhealthkmp.data.diary.sources

import com.knthcame.myhealthkmp.data.diary.model.HeartRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class FakeDiaryDao(
    coroutineScope: CoroutineScope,
) : DiaryDao {
    private val now get() = Clock.System.now()
    private val cadence = 5.minutes
    private val _heartRates: StateFlow<List<HeartRate>> =
        flow {
            val heartRateMeasurements = initReadings().toMutableList()

            while (true) {
                val lastValue = heartRateMeasurements.last().bpm
                heartRateMeasurements.add(generateNextValue(lastValue, now))

                emit(heartRateMeasurements.toList())

                delay(cadence)
            }
        }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    override val heartRates: Flow<List<HeartRate>> = _heartRates

    private fun initReadings(): List<HeartRate> =
        buildList {
            var timeStamp = now.minus(5.hours)
            var bpm = 90.0

            do {
                val nextMeasurement = generateNextValue(bpm, timeStamp)
                bpm = nextMeasurement.bpm
                add(nextMeasurement)
                timeStamp += cadence
            } while (timeStamp < now)
        }

    private fun generateNextValue(
        currentValue: Double,
        timeStamp: Instant,
    ): HeartRate {
        val minValue = 60.0
        val maxValue = 130.0
        val maxStep = 10.0
        val step = (Random.nextFloat() * maxStep * 2) - maxStep

        val nextValue = max(min(currentValue + step, maxValue), minValue)
        return HeartRate(nextValue.round(1), timeStamp)
    }

    private fun Double.round(decimals: Int): Double {
        val factor = 10.0.pow(decimals)
        return ((this * factor).roundToInt() / factor)
    }
}
