package com.squaredcandy.waypoint.core.feature

import androidx.compose.runtime.State
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import kotlin.reflect.KClass

class RealWaypointContext(
    waypoint: Waypoint,
    private val mutableWaypointHolderState: State<MutableWaypointHolder?>,
    private val waypointActionProviderState: State<WaypointActionProvider>,
) : WaypointContext {
    override val waypointId: Identifier<Waypoint> = waypoint.id

    override fun <T : WaypointAction> sendAction(
        waypointActionClass: KClass<T>,
        waypointAction: T,
    ): Result<Unit> {
        return runCatching {
            val mutableWaypointHolder = mutableWaypointHolderState.value!!
            val actionProvider = waypointActionProviderState.value
            val action = actionProvider.getAction(waypointActionClass)!!
            action.invoke(mutableWaypointHolder, waypointAction)
        }
    }
}
