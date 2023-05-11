package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList

interface WaypointRoute {
    val waypointList: ImmutableList<Waypoint>
}
