package com.squaredcandy.waypoint.core.handle

import androidx.compose.ui.modifier.ModifierLocalReadScope
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint

interface WaypointHandleProvider {
    fun <T : WaypointHandle> buildWaypointHandle(
        constructor: (Identifier<Waypoint>, ModifierLocalReadScope) -> T,
        waypoint: Waypoint
    ): T
}
