package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.plus
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap

private val ModifierLocalWaypointActionMap = modifierLocalOf<WaypointActionMap?> { null }

internal class WaypointActionNode(
    var mergeParentActions: Boolean,
    initialWaypointActionMap: WaypointActionMap,
) : ModifierLocalModifierNode, SemanticsModifierNode, Modifier.Node() {

    var waypointActionMap by mutableStateOf(initialWaypointActionMap)

    private val parentWaypointActionMap: WaypointActionMap?
        get() = if (isAttached) ModifierLocalWaypointActionMap.current else null

    private val waypointActionProvider by derivedStateOf {
        val parentMap = if (mergeParentActions) parentWaypointActionMap else null
        val mergedWaypointActionMap = parentMap + waypointActionMap
        WaypointActionProvider { actionClass ->
            val hooks = mergedWaypointActionMap.hooks
            val waypointActionResolver = mergedWaypointActionMap.resolvers[actionClass]
            if (waypointActionResolver != null) {
                WaypointActionResolver { waypointHolder, waypointAction ->
                    hooks.forEach { hook -> hook.preResolveHook(waypointHolder, waypointAction) }
                    waypointActionResolver.invoke(waypointHolder, waypointAction)
                    hooks.forEach { hook -> hook.postResolveHook(waypointHolder, waypointAction) }
                }
            } else {
                null
            }
        }
    }

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointActionProvider to waypointActionProvider,
            ModifierLocalWaypointActionMap to waypointActionMap,
        )

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[SemanticsProperties.WaypointActionProviderSemanticsKey] = waypointActionProvider
    }
}

private operator fun WaypointActionMap?.plus(otherWaypointActionMap: WaypointActionMap): WaypointActionMap {
    return if (this != null) {
        WaypointActionMap(
            resolvers = this.resolvers.toPersistentMap().putAll(otherWaypointActionMap.resolvers),
            hooks = this.hooks.plus(otherWaypointActionMap.hooks).toImmutableList(),
        )
    } else {
        otherWaypointActionMap
    }
}
