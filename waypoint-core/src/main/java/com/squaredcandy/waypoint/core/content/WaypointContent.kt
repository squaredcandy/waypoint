package com.squaredcandy.waypoint.core.content

import androidx.compose.runtime.Composable
import com.squaredcandy.waypoint.core.feature.WaypointContext

fun interface WaypointContent {
    context(WaypointContext)
    @Composable
    fun Content()
}
