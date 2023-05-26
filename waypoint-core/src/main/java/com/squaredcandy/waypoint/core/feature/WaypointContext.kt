package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder

data class WaypointContext(
    val canBacktrack: Boolean,
    val waypointId: Identifier<Waypoint>,
    val mutableWaypointHolder: MutableWaypointHolder,
    val waypointActionProvider: WaypointActionProvider,
)
