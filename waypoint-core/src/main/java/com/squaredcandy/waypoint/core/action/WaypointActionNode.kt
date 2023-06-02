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
import kotlin.reflect.KClass

private val ModifierLocalWaypointActionSetSource = modifierLocalOf<WaypointActionSetSource?> { null }

internal class WaypointActionNode(
    private var mergeParentActions: Boolean,
    initialWaypointActionSet: WaypointActionSet,
) : ModifierLocalModifierNode, SemanticsModifierNode, Modifier.Node() {

    private val selfWaypointActionSetSource: WaypointActionSetSource = WaypointActionSetSource()
    private val parentWaypointActionSetSource: WaypointActionSetSource?
        get() = if (isAttached) ModifierLocalWaypointActionSetSource.current else null

    private val waypointActionSetSource: WaypointActionSetSource
        get() = parentWaypointActionSetSource.takeIf { mergeParentActions }
            ?: selfWaypointActionSetSource

    private var waypointActionSet by mutableStateOf(initialWaypointActionSet)

    private val waypointActionProvider by derivedStateOf {
        val mergedWaypointActionMap = waypointActionSetSource.waypointActionMap
        object : WaypointActionProvider {
            @Suppress("UNCHECKED_CAST")
            override fun <T : WaypointAction> getAction(waypointActionClass: KClass<T>): WaypointActionResolver<T>? {
                val hooks = mergedWaypointActionMap.hooks
                val waypointActionResolver = mergedWaypointActionMap.resolvers[waypointActionClass] as? WaypointActionResolver<T>
                return if (waypointActionResolver != null) {
                    WaypointActionResolver { waypointHolder, waypointAction ->
                        hooks.forEach { hook -> hook.preResolveHook(waypointHolder, waypointAction) }
                        waypointActionResolver(waypointHolder, waypointAction)
                        hooks.forEach { hook -> hook.postResolveHook(waypointHolder, waypointAction) }
                    }
                } else {
                    null
                }
            }
        }
    }

    override fun onAttach() {
        updateWaypointActionSetSources()
    }

    override fun onDetach() {
        selfWaypointActionSetSource.removeIf { it.first == this }
        if (selfWaypointActionSetSource != waypointActionSetSource) {
            waypointActionSetSource.removeIf { it.first == this }
        }
    }

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[SemanticsProperties.WaypointActionProviderSemanticsKey] = waypointActionProvider
    }

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointActionProvider to waypointActionProvider,
            ModifierLocalWaypointActionSetSource to waypointActionSetSource,
        )

    fun updateWaypointAction(
        mergeParentActions: Boolean,
        waypointActionSet: WaypointActionSet,
    ) {
        this.mergeParentActions = mergeParentActions
        this.waypointActionSet = waypointActionSet
        updateWaypointActionSetSources()
    }

    private fun updateWaypointActionSetSources() {
        selfWaypointActionSetSource.removeIf { it.first == this }
        waypointActionSetSource.removeIf { it.first == this }
        selfWaypointActionSetSource.add(this to waypointActionSet)
        if (waypointActionSetSource != selfWaypointActionSetSource) {
            waypointActionSetSource.add(this to waypointActionSet)
        }
    }
}
