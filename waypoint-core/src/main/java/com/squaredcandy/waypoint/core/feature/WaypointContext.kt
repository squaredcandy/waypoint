package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import kotlin.reflect.KClass

interface WaypointContext {
    val waypointId: Identifier<Waypoint>
    fun <T: WaypointAction> sendAction(waypointActionClass: KClass<T>, waypointAction: T): Result<Unit>
}
