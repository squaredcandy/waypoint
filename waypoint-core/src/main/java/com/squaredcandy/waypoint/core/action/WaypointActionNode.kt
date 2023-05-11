package com.squaredcandy.waypoint.core.action

import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalNode
import androidx.compose.ui.modifier.modifierLocalMapOf

internal class WaypointActionNode(
    var waypointActionMap: WaypointActionMap,
) : ModifierLocalNode, Modifier.Node() {

    private val waypointActionProvider = WaypointActionProvider { actionClass ->
        val hooks = waypointActionMap.hooks[actionClass]
        val waypointActionResolver = waypointActionMap.resolvers[actionClass]
        if (hooks != null && waypointActionResolver != null) {
            WaypointActionResolver { waypointHolder, waypointAction ->
                hooks.forEach { hook -> hook.preResolveHook(waypointHolder, waypointAction) }
                waypointActionResolver.invoke(waypointHolder, waypointAction)
                hooks.forEach { hook -> hook.postResolveHook(waypointHolder, waypointAction) }
            }
        } else {
            waypointActionResolver
        }
    }

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointActionProvider to waypointActionProvider
        )
}
