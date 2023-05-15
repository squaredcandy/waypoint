package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.modifier.modifierLocalOf
import kotlinx.collections.immutable.toPersistentMap

private val ModifierLocalWaypointActionMap = modifierLocalOf<WaypointActionMap?> { null }

internal class WaypointActionNode(
    var mergeParentActions: Boolean,
    initialWaypointActionMap: WaypointActionMap,
) : ModifierLocalNode, Modifier.Node() {

    var waypointActionMap by mutableStateOf(initialWaypointActionMap)

    private val parentWaypointActionMap: WaypointActionMap?
        get() = if (isAttached) ModifierLocalWaypointActionMap.current else null

    private val waypointActionProvider by derivedStateOf {
        val parentMap = if (mergeParentActions) parentWaypointActionMap else null
        val mergedWaypointActionMap = parentMap + waypointActionMap
        WaypointActionProvider { actionClass ->
            val hooks = mergedWaypointActionMap.hooks[actionClass]
            val waypointActionResolver = mergedWaypointActionMap.resolvers[actionClass]
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
            ModifierLocalWaypointActionProvider to waypointActionProvider,
            ModifierLocalWaypointActionMap to waypointActionMap,
        )
}

private operator fun WaypointActionMap?.plus(otherWaypointActionMap: WaypointActionMap): WaypointActionMap {
    return if (this != null) {
        WaypointActionMap(
            resolvers = this.resolvers.toPersistentMap().putAll(otherWaypointActionMap.resolvers),
            hooks = this.hooks.toPersistentMap().putAll(otherWaypointActionMap.hooks),
        )
    } else {
        otherWaypointActionMap
    }
}
