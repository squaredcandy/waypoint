package com.squaredcandy.waypoint.core.route

import androidx.compose.runtime.compositionLocalOf
import com.squaredcandy.waypoint.core.Waypoint

val LocalWaypoint = compositionLocalOf<Waypoint> { error("Missing Waypoint") }
val LocalCanBacktrack = compositionLocalOf { false }
