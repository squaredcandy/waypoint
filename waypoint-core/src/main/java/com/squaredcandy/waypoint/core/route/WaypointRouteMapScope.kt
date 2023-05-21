package com.squaredcandy.waypoint.core.route

@WaypointRouteMapScopeDsl
interface WaypointRouteMapScope {
    fun <T : WaypointRoute<T>> addRoute(generateWaypointRoute: GenerateWaypointRoute<T>)
}
