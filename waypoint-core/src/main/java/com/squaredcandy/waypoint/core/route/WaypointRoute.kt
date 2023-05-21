package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface WaypointRoute<T> {
    val waypointList: ImmutableList<Waypoint>

    val key: Identifier<T>

    companion object : WaypointRoute<MainWaypointRoute> {
        override val waypointList: ImmutableList<Waypoint> = persistentListOf()
        override val key: Identifier<MainWaypointRoute> = Identifier("Test")
    }
}
