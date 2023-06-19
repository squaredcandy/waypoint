package com.squaredcandy.waypoint.bottom_nav.emails

import androidx.compose.runtime.Immutable
import java.io.Serializable
import java.time.ZonedDateTime

@Immutable
data class Email(
    val id: String,
    val from: String,
    val to: String,
    val title: String,
    val message: String,
    val starred: Boolean,
    val date: ZonedDateTime,
): Serializable
