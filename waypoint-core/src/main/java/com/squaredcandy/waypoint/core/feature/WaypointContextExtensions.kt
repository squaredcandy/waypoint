package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.action.WaypointAction

/**
 * Send a waypoint action
 */
context(WaypointContext)
inline fun <reified T: WaypointAction> sendAction(waypointAction: T): Result<Unit> {
    return sendAction(T::class, waypointAction)
}
