package com.squaredcandy.waypoint.util

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

internal fun ZonedDateTime.formatDate(formatStyle: FormatStyle = FormatStyle.SHORT): String {
    val currentDate = ZonedDateTime.now()
    return when (ChronoUnit.DAYS.between(currentDate, this)) {
        0L -> "Today"
        1L -> "Yesterday"
        else -> this.format(DateTimeFormatter.ofLocalizedDate(formatStyle))
    }
}
