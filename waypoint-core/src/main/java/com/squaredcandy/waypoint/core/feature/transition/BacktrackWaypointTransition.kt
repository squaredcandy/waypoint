package com.squaredcandy.waypoint.core.feature.transition

interface BacktrackWaypointTransition : WaypointTransition {
    override fun backtrackWaypointTransition(): WaypointTransition = this
}
