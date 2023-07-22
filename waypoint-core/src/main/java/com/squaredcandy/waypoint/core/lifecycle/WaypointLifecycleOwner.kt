package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Waypoint

val ModifierLocalWaypointLifecycleOwner = modifierLocalOf<WaypointLifecycleOwner> { EmptyWaypointLifecycleOwner() }

interface WaypointLifecycleOwner {
    @Composable
    fun WithLifecycle(
        waypoint: Waypoint,
        block: @Composable () -> Unit
    )
}
