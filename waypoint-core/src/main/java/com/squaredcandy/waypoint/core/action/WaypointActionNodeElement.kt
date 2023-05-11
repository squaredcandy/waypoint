package com.squaredcandy.waypoint.core.action

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointActionNodeElement(
    val waypointActionMap: WaypointActionMap,
) : ModifierNodeElement<WaypointActionNode>() {
    override fun create(): WaypointActionNode = WaypointActionNode(waypointActionMap)

    override fun update(node: WaypointActionNode): WaypointActionNode {
        node.waypointActionMap = waypointActionMap
        return node
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Action Node"
    }
}
