package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsConfiguration
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointRouteProviderSemanticsKey

internal class WaypointRouteProviderNode(
    var waypointRouteGenerator: WaypointRouteGenerator,
) : ModifierLocalNode, SemanticsModifierNode, Modifier.Node() {
    private val waypointRouteProvider: WaypointRouteProvider?
        get() = if (isAttached) {
            val waypointHolder = ModifierLocalWaypointHolder.current
                ?: throw IllegalStateException("Missing Waypoint Holder")
            RealWaypointRouteProvider(
                waypointHolder = waypointHolder,
                waypointRouteGenerator = waypointRouteGenerator,
            )
        } else {
            null
        }

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointRouteProvider to waypointRouteProvider,
        )

    override val semanticsConfiguration: SemanticsConfiguration
        get() = SemanticsConfiguration()
            .apply {
                this[WaypointRouteProviderSemanticsKey] = waypointRouteProvider
            }
}
