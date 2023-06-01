package com.squaredcandy.waypoint.core.action

import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder

fun interface WaypointActionResolver<T : WaypointAction> {
    operator fun invoke(waypointHolder: MutableWaypointHolder, waypointAction: T)
}

inline fun <reified T: WaypointAction> waypointActionResolver(
    noinline block: (waypointHolder: MutableWaypointHolder, waypointAction: T) -> Unit,
): WaypointActionResolver<T> = WaypointActionResolver(block)
