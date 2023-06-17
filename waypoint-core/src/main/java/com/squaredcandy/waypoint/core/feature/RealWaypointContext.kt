package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import kotlin.reflect.KClass

class RealWaypointContext(
    waypoint: Waypoint,
    private val mutableWaypointHolder: MutableWaypointHolder?,
    private val waypointActionProvider: WaypointActionProvider,
) : WaypointContext {
    override val waypointId: Identifier<Waypoint> = waypoint.id

    override fun <T : WaypointAction> sendAction(
        waypointActionClass: KClass<T>,
        waypointAction: T,
    ): Result<Unit> {
        return runCatching {
            val actionProvider = waypointActionProvider
            val action = actionProvider.getAction(waypointActionClass)!!
            action.invoke(mutableWaypointHolder!!, waypointAction)
        }
    }
}
