package com.squaredcandy.waypoint.core.feature

import androidx.compose.runtime.Composable

fun interface WaypointContent {
    context(WaypointContext)
    @Composable
    fun Content()
}
