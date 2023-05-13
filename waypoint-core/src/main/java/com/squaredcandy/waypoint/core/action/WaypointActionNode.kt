package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalNode
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.modifier.modifierLocalMapOf

internal class WaypointActionNode(
    var waypointActionMapBuilderState: State<context(ModifierLocalReadScope) WaypointActionMapBuilder.() -> Unit>,
) : ModifierLocalNode, Modifier.Node() {

    private val waypointActionProvider by derivedStateOf {
        val waypointActionMap = buildWaypointActions(waypointActionMapBuilderState.value)
        WaypointActionProvider { actionClass ->
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
    }

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointActionProvider to waypointActionProvider
        )
}
