package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import kotlinx.collections.immutable.ImmutableMap

class WaypointRouteProviderNode(
    var generateWaypointRouteMap: ImmutableMap<Identifier<WaypointRouteKey>, GenerateWaypointRoute>,
) : ModifierLocalNode, Modifier.Node() {
    private val waypointRouteProvider: WaypointRouteProvider
        get() = if (isAttached) {
            val waypointHolder = ModifierLocalWaypointHolder.current
                ?: throw IllegalStateException("Missing Waypoint Holder")
            RealWaypointRouteProvider(
                waypointHolder = waypointHolder,
                generateWaypointRouteMap = generateWaypointRouteMap,
            )
        } else {
            EmptyWaypointRouteProvider()
        }

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointRouteProvider to waypointRouteProvider,
        )
}
