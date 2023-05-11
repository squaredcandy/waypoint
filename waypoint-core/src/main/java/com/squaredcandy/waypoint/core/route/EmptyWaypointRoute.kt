package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class EmptyWaypointRoute : WaypointRoute {
    override val waypointList: ImmutableList<Waypoint> = persistentListOf()
}
