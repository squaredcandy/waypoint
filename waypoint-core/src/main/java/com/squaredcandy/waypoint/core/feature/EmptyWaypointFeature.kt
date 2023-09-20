package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.content.EmptyWaypointContent
import com.squaredcandy.waypoint.core.content.WaypointContent

/**
 * An empty waypoint feature for convenience.
 */
data object EmptyWaypointFeature : WaypointFeature {
    override fun getContent(): WaypointContent = EmptyWaypointContent
    private fun readResolve(): Any = EmptyWaypointFeature
}
