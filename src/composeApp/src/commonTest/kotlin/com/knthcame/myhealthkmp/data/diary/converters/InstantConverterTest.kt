package com.knthcame.myhealthkmp.data.diary.converters

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals

class InstantConverterTest {
    private val converter = InstantConverter()

    @Test
    fun fromTimestamp_returnsInstant_forProvidedTimestamp() {
        val fromEpochMilliseconds = 546548448645645

        val actual = converter.fromTimestamp(fromEpochMilliseconds)

        assertEquals(Instant.fromEpochMilliseconds(fromEpochMilliseconds), actual)
    }

    @Test
    fun instantToTimestamp_returnsFromEpochMilliseconds_forProvidedInstant() {
        val instant = Clock.System.now()

        val actual = converter.instantToTimestamp(instant)

        assertEquals(instant.toEpochMilliseconds(), actual)
    }
}