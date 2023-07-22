package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.Composable
import com.squaredcandy.waypoint.core.Waypoint

class EmptyWaypointLifecycleOwner : WaypointLifecycleOwner {
    @Composable
    override fun WithLifecycle(
        waypoint: Waypoint,
        block: @Composable () -> Unit,
    ) {
        block()
    }
}
