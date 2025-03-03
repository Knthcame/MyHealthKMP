package com.knthcame.myhealthkmp.ui.common

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.DateTimeException
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DateTimeFormatsTest {
    private lateinit var localTestDateTime: LocalDateTime
    private lateinit var javaDateTime: java.time.LocalDateTime

    @BeforeTest
    fun beforeTest() {
        localTestDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        javaDateTime = localTestDateTime.toJavaLocalDateTime()
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDateTime_onShortFormat() {
        val expected = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(javaDateTime)

        val actual = localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Short)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDateTime_onMediumFormat() {
        val expected =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(javaDateTime)

        val actual = localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Medium)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_throwsDateTimeException_onLongDateTimeFormat() {
        assertFailsWith<DateTimeException> {
            localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Long)
        }
    }

    @Test
    fun formatWithCurrentLocale_throwsException_onFullDateTimeFormat() {
        assertFailsWith<DateTimeException> {
            localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Long)
        }
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onShortFormat() {
        val expected = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(javaDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Short)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onMediumFormat() {
        val expected = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(javaDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Medium)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onLongFormat() {
        val expected = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(javaDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Long)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onFullFormat() {
        val expected = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(javaDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Full)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedTime_onShortFormat() {
        val expected = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(javaDateTime)

        val actual = localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Short)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedTime_onMediumFormat() {
        val expected = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM).format(javaDateTime)

        val actual = localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Medium)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_throwsDateTimeException_onLongTimeFormat() {
        assertFailsWith<DateTimeException> {
            localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Long)
        }
    }

    @Test
    fun formatWithCurrentLocale_throwsException_onFullTimeFormat() {
        assertFailsWith<DateTimeException> {
            localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Long)
        }
    }
}