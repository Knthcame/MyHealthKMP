package com.knthcame.myhealthkmp.ui.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

actual fun LocalDateTime.formatWithCurrentLocale(dateTimeStyle: DateTimeStyle): String {
    val formatter = DateTimeFormatter.ofLocalizedDateTime(dateTimeStyle.toPlatform())
    return toJavaLocalDateTime().format(formatter)
}

actual fun LocalDate.formatWithCurrentLocale(dateStyle: DateTimeStyle): String {
    val formatter = DateTimeFormatter.ofLocalizedDate(dateStyle.toPlatform())
    return toJavaLocalDate().format(formatter)
}

actual fun LocalTime.formatWithCurrentLocale(timeStyle: DateTimeStyle): String {
    val formatter = DateTimeFormatter.ofLocalizedTime(timeStyle.toPlatform())
    return toJavaLocalTime().format(formatter)
}

private fun DateTimeStyle.toPlatform(): FormatStyle = when (this) {
    DateTimeStyle.Short -> FormatStyle.SHORT
    DateTimeStyle.Medium -> FormatStyle.MEDIUM
    DateTimeStyle.Long -> FormatStyle.LONG
    DateTimeStyle.Full -> FormatStyle.FULL
}