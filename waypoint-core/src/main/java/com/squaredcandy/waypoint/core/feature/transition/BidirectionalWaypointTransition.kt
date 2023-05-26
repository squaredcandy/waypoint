package com.squaredcandy.waypoint.core.feature.transition

interface BidirectionalWaypointTransition : WaypointTransition {
    override fun navigateWaypointTransition(): WaypointTransition = this
    override fun backtrackWaypointTransition(): WaypointTransition = this
}
