package com.knthcame.myhealthkmp.ui.common

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

class DateTimeFormatsTest {
    private lateinit var localTestDateTime: LocalDateTime
    private lateinit var nsDateTime: NSDate

    @BeforeTest
    fun beforeTest() {
        localTestDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        nsDateTime =
            NSCalendar.currentCalendar.dateFromComponents(localTestDateTime.toNSDateComponents())!!
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDateTime_onShortFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterShortStyle
        formatter.timeStyle = NSDateFormatterShortStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Short)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDateTime_onMediumFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterMediumStyle
        formatter.timeStyle = NSDateFormatterMediumStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Medium)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDateTime_onLongFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterLongStyle
        formatter.timeStyle = NSDateFormatterLongStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Long)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDateTime_onFullFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterFullStyle
        formatter.timeStyle = NSDateFormatterFullStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.formatWithCurrentLocale(DateTimeStyle.Full)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onShortFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterShortStyle
        formatter.timeStyle = NSDateFormatterNoStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Short)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onMediumFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterMediumStyle
        formatter.timeStyle = NSDateFormatterNoStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Medium)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onLongFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterLongStyle
        formatter.timeStyle = NSDateFormatterNoStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Long)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedDate_onFullFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterFullStyle
        formatter.timeStyle = NSDateFormatterNoStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.date.formatWithCurrentLocale(DateTimeStyle.Full)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedTime_onShortFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterNoStyle
        formatter.timeStyle = NSDateFormatterShortStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Short)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedTime_onMediumFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterNoStyle
        formatter.timeStyle = NSDateFormatterMediumStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Medium)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedTime_onLongFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterNoStyle
        formatter.timeStyle = NSDateFormatterLongStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Long)

        assertEquals(expected, actual)
    }

    @Test
    fun formatWithCurrentLocale_returnsFormattedTime_onFullFormat() {
        val formatter = NSDateFormatter()
        formatter.dateStyle = NSDateFormatterNoStyle
        formatter.timeStyle = NSDateFormatterFullStyle
        formatter.locale = NSLocale.currentLocale
        val expected = formatter.stringFromDate(nsDateTime)

        val actual = localTestDateTime.time.formatWithCurrentLocale(DateTimeStyle.Full)

        assertEquals(expected, actual)
    }
}