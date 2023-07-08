package com.squaredcandy.waypoint.core

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
@Suppress("unused")
@JvmInline
value class WaypointTag(private val tag: String): Serializable by tag, Comparable<String> by tag
