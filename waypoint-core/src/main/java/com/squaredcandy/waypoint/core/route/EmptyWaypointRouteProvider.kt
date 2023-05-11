package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier

class EmptyWaypointRouteProvider : WaypointRouteProvider {
    override fun getRoute(key: Identifier<WaypointRouteKey>): WaypointRoute = throw IllegalStateException("No routes provided")
}
