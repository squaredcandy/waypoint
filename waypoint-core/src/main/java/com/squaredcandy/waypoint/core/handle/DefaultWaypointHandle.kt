package com.squaredcandy.waypoint.core.handle

import androidx.compose.ui.modifier.ModifierLocalReadScope
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.ModifierLocalWaypointActionProvider
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import kotlin.reflect.KClass

class DefaultWaypointHandle(
    waypointId: Identifier<Waypoint>,
    modifierLocalReadScope: ModifierLocalReadScope,
) : WaypointHandle(
    waypointId = waypointId,
    modifierLocalReadScope = modifierLocalReadScope,
) {
    private val mutableWaypointHolder: MutableWaypointHolder = ModifierLocalMutableWaypointHolder.current
    private val waypointActionProvider: WaypointActionProvider = ModifierLocalWaypointActionProvider.current

    fun <T: WaypointAction> sendAction(
        waypointActionClass: KClass<T>,
        waypointAction: T,
    ): Result<Unit> {
        return runCatching {
            val actionProvider = waypointActionProvider
            val action = actionProvider.getAction(waypointActionClass)!!
            action.invoke(mutableWaypointHolder, waypointAction)
        }
    }
}

inline fun <reified T: WaypointAction> DefaultWaypointHandle.sendAction(waypointAction: T): Result<Unit> {
    return sendAction(T::class, waypointAction)
}
