package com.knthcame.myhealthkmp.ui.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atDate
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterNoStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun LocalDateTime.formatWithCurrentLocale(dateTimeStyle: DateTimeStyle): String {
    val date = NSCalendar.currentCalendar.dateFromComponents(this.toNSDateComponents())!!

    val formatter = NSDateFormatter()
    formatter.dateStyle = dateTimeStyle.toPlatform()
    formatter.timeStyle = dateTimeStyle.toPlatform()
    formatter.locale = NSLocale.currentLocale

    return formatter.stringFromDate(date)
}

actual fun LocalDate.formatWithCurrentLocale(dateStyle: DateTimeStyle): String {
    val date = NSCalendar.currentCalendar.dateFromComponents(this.toNSDateComponents())!!

    val formatter = NSDateFormatter()
    formatter.dateStyle = dateStyle.toPlatform()
    formatter.timeStyle = NSDateFormatterNoStyle
    formatter.locale = NSLocale.currentLocale

    return formatter.stringFromDate(date)
}

actual fun LocalTime.formatWithCurrentLocale(timeStyle: DateTimeStyle): String {
    val dateComponents = this.atDate(LocalDate.fromEpochDays(0)).toNSDateComponents()
    val dateTime = NSCalendar.currentCalendar.dateFromComponents(dateComponents)!!

    val formatter = NSDateFormatter()
    formatter.dateStyle = NSDateFormatterNoStyle
    formatter.timeStyle = timeStyle.toPlatform()
    formatter.locale = NSLocale.currentLocale

    return formatter.stringFromDate(dateTime)
}

private fun DateTimeStyle.toPlatform(): ULong =
    when (this) {
        DateTimeStyle.Short -> NSDateFormatterShortStyle
        DateTimeStyle.Medium -> NSDateFormatterMediumStyle
        DateTimeStyle.Long -> NSDateFormatterLongStyle
        DateTimeStyle.Full -> NSDateFormatterFullStyle
    }
