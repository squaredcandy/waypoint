package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.holder.WaypointHolder

fun interface GenerateWaypointRoute<T : WaypointRoute<T>> {
    fun generate(waypointHolder: WaypointHolder): WaypointRoute<T>
}
