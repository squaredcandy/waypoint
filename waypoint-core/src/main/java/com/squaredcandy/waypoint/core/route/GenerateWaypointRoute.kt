package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.holder.WaypointHolder

fun interface GenerateWaypointRoute {
    fun generate(waypointHolder: WaypointHolder): WaypointRoute
}
