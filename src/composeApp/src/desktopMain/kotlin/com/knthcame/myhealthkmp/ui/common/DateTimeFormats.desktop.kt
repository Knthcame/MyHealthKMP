package com.knthcame.myhealthkmp.ui.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

actual fun LocalDateTime.formatWithCurrentLocale(dateTimeStyle: DateTimeStyle): String {
    val javaDate = java.time.LocalDateTime.of(year, month, dayOfMonth, hour, minute, second)
    val formatter = DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle.toPlatform())
    return javaDate.format(formatter)
}

actual fun LocalDate.formatWithCurrentLocale(dateStyle: DateTimeStyle): String {
    val javaDate = java.time.LocalDate.of(year, month, dayOfMonth)
    val formatter = DateTimeFormatter.ofLocalizedDate(dateStyle.toPlatform())
    return javaDate.format(formatter)
}

actual fun LocalTime.formatWithCurrentLocale(timeStyle: DateTimeStyle): String {
    val javaTime = java.time.LocalTime.of(hour, minute, second)
    val formatter = DateTimeFormatter.ofLocalizedTime(timeStyle.toPlatform())
    return javaTime.format(formatter)
}

private fun DateTimeStyle.toPlatform(): FormatStyle = when (this) {
    DateTimeStyle.Short -> FormatStyle.SHORT
    DateTimeStyle.Medium -> FormatStyle.MEDIUM
    DateTimeStyle.Long -> FormatStyle.LONG
    DateTimeStyle.Full -> FormatStyle.FULL
}