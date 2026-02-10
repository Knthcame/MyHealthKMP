package com.knthcame.myhealthkmp.ui.common

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

enum class DateTimeStyle {
    Short,
    Medium,
    Long,
    Full,
}

/**
 *  Formats the local date according to the current system's locale.
 *
 * @param dateTimeStyle the format style to be used. Only [DateTimeStyle.Short] and [DateTimeStyle.Medium]
 * are supported on Android & Desktop.
 */
expect fun LocalDateTime.formatWithCurrentLocale(dateTimeStyle: DateTimeStyle = DateTimeStyle.Medium): String

/**
 *  Formats the local date according to the current system's locale.
 *
 *  @param dateStyle the format style to be used.
 */
expect fun LocalDate.formatWithCurrentLocale(dateStyle: DateTimeStyle = DateTimeStyle.Long): String

/**
 * Formats the local time according to the current system's locale.
 *
 * @param timeStyle the format style to be used. Only [DateTimeStyle.Short] and [DateTimeStyle.Medium]
 * are supported on Android & Desktop.
 */
expect fun LocalTime.formatWithCurrentLocale(timeStyle: DateTimeStyle = DateTimeStyle.Short): String