package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier

@WaypointRouteMapScopeDsl
interface WaypointRouteMapScope {
    fun addRoute(key: Identifier<WaypointRouteKey>, generateWaypointRoute: GenerateWaypointRoute)
}
