package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalNode
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.modifier.modifierLocalOf
import kotlinx.collections.immutable.toPersistentMap

private val ModifierLocalWaypointActionMap = modifierLocalOf<WaypointActionMap?> { null }

internal class WaypointActionNode(
    var mergeParentActions: Boolean,
    var waypointActionMapBuilderState: State<context(ModifierLocalReadScope) WaypointActionMapBuilder.() -> Unit>,
) : ModifierLocalNode, Modifier.Node() {

    private val parentWaypointActionMap: WaypointActionMap?
        get() = if (isAttached) ModifierLocalWaypointActionMap.current else null

    private val waypointActionProvider by derivedStateOf {
        val parentMap = if (mergeParentActions) parentWaypointActionMap else null
        val waypointActionMap = buildWaypointActions(waypointActionMapBuilderState.value)
            .let { currentMap ->
                if (parentMap != null) parentMap + currentMap else currentMap
            }
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
            ModifierLocalWaypointActionProvider to waypointActionProvider,
            ModifierLocalWaypointActionMap to buildWaypointActions(waypointActionMapBuilderState.value),
        )
}

private operator fun WaypointActionMap.plus(otherWaypointActionMap: WaypointActionMap): WaypointActionMap {
    return WaypointActionMap(
        resolvers = this.resolvers.toPersistentMap().putAll(otherWaypointActionMap.resolvers),
        hooks = this.hooks.toPersistentMap().putAll(otherWaypointActionMap.hooks),
    )
}
