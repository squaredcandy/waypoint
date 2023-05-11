package com.squaredcandy.waypoint.core.action

import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder

fun interface WaypointActionResolver {
    operator fun invoke(waypointHolder: MutableWaypointHolder, waypointAction: WaypointAction)
}

inline fun <reified T: WaypointAction> waypointActionResolver(
    silentFail: Boolean = false,
    noinline block: (waypointHolder: MutableWaypointHolder, waypointAction: T) -> Unit,
): WaypointActionResolver = WaypointActionResolver { waypointHolder, waypointAction ->
    if (waypointAction is T) {
        block(waypointHolder, waypointAction)
    } else if (!silentFail) {
        throw IllegalStateException("$waypointAction was not of type ${T::class}")
    }
}
