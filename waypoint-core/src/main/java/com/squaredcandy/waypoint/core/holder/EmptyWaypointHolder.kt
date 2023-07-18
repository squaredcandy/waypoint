package com.squaredcandy.waypoint.core.holder

import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal object EmptyWaypointHolder : MutableWaypointHolder {
    override val parent: MutableWaypointHolder? = null
    override val isDefined: Boolean = false
    override val waypointList: ImmutableList<Waypoint> = persistentListOf()
    override val lastNavigationType: WaypointNavigationType? = null

    override fun <T> updateWaypointList(
        navigationType: WaypointNavigationType,
        update: MutableList<Waypoint>.() -> T
    ): T {
        error("EmptyWaypointHolder is not modifiable")
    }
}
