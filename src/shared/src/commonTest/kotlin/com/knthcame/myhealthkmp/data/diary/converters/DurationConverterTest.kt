package com.knthcame.myhealthkmp.data.diary.converters

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.nanoseconds

class DurationConverterTest {
    private val converter = DurationConverter()

    @Test
    fun fromNanoseconds_returnsEquivalentDuration_onCall() {
        val duration = 9005000L

        val actual = converter.fromNanoseconds(duration)

        assertEquals(duration.nanoseconds, actual)
    }


    @Test
    fun durationToNanoseconds_returnsWholeNanoseconds_onCall() {
        val duration = 3005040689.nanoseconds

        val actual = converter.durationToNanoseconds(duration)

        assertEquals(duration.inWholeNanoseconds, actual)
    }
}