package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.holder.WaypointHolder

data class WaypointContext(
    val waypointId: Identifier<Waypoint>,
    val waypointHolder: WaypointHolder,
    val waypointActionProvider: WaypointActionProvider,
)
