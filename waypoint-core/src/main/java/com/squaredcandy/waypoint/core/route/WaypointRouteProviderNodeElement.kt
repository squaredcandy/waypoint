package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointRouteProviderNodeElement(
    val waypointRouteGenerator: WaypointRouteGenerator,
) : ModifierNodeElement<WaypointRouteProviderNode>() {
    override fun create(): WaypointRouteProviderNode = WaypointRouteProviderNode(waypointRouteGenerator)

    override fun update(node: WaypointRouteProviderNode) {
        node.waypointRouteGenerator = waypointRouteGenerator
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Route Provider Node"
    }
}
