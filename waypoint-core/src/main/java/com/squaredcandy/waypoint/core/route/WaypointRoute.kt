package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList

interface WaypointRoute<T> {
    val key: Identifier<T>
    val waypointList: ImmutableList<Waypoint>
    val canBacktrack: Boolean
}
