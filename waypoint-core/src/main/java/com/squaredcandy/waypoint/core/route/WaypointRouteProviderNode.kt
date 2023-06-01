package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointRouteProviderSemanticsKey

internal class WaypointRouteProviderNode(
    var waypointRouteGenerator: WaypointRouteGenerator,
) : ModifierLocalModifierNode, SemanticsModifierNode, Modifier.Node() {
    private val waypointRouteProvider: WaypointRouteProvider?
        get() {
            if (!isAttached) return null
            return RealWaypointRouteProvider(
                waypointHolder = ModifierLocalWaypointHolder.current ?: return null,
                waypointRouteGenerator = waypointRouteGenerator,
            )
        }

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointRouteProvider to waypointRouteProvider,
        )

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[WaypointRouteProviderSemanticsKey] = waypointRouteProvider
    }
}
