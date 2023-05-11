package com.squaredcandy.waypoint.core.action

import com.squaredcandy.waypoint.core.holder.WaypointHolder

/**
 * Hook into a waypoint action.
 */
interface WaypointActionHook {
    fun preResolveHook(waypointHolder: WaypointHolder, waypointAction: WaypointAction)
    fun postResolveHook(waypointHolder: WaypointHolder, waypointAction: WaypointAction)
}
