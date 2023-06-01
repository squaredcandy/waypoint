package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.action.getAction

/**
 * Send a waypoint action
 */
context(WaypointContext)
inline fun <reified T: WaypointAction> sendAction(waypointAction: T): Result<Unit> {
    return runCatching {
        val actionProvider = waypointActionProvider
        val action = actionProvider.getAction<T>()!!
        action(mutableWaypointHolder, waypointAction)
    }
}
