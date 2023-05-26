package com.squaredcandy.waypoint.core.feature.transition

interface NavigateWaypointTransition : WaypointTransition {
    override fun navigateWaypointTransition(): WaypointTransition = this
}
