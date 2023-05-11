package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import com.squaredcandy.waypoint.core.Identifier
import kotlinx.collections.immutable.ImmutableMap

internal data class WaypointRouteProviderNodeElement(
    val generateWaypointRouteMap: ImmutableMap<Identifier<WaypointRouteKey>, GenerateWaypointRoute>,
) : ModifierNodeElement<WaypointRouteProviderNode>() {
    override fun create(): WaypointRouteProviderNode = WaypointRouteProviderNode(generateWaypointRouteMap)

    override fun update(node: WaypointRouteProviderNode): WaypointRouteProviderNode {
        node.generateWaypointRouteMap = generateWaypointRouteMap
        return node
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Route Provider Node"
    }
}
