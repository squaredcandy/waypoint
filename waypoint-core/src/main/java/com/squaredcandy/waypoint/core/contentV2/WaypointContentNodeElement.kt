package com.squaredcandy.waypoint.core.contentV2

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointContentNodeElement(
    val waypointContentDefinition: WaypointContentDefinition,
) : ModifierNodeElement<WaypointContentNode>() {
    override fun create(): WaypointContentNode = WaypointContentNode(waypointContentDefinition)

    override fun update(node: WaypointContentNode) {
        node.updateWaypointContentDefinition(waypointContentDefinition)
    }

    override fun InspectorInfo.inspectableProperties() {}
}
