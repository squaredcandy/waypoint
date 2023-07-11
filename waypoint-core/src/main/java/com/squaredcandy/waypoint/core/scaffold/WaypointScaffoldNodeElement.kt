package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointScaffoldNodeElement(
    val waypointScaffoldDefinition: WaypointScaffoldDefinition,
) : ModifierNodeElement<WaypointScaffoldNode>() {
    override fun create(): WaypointScaffoldNode = WaypointScaffoldNode(waypointScaffoldDefinition)

    override fun update(node: WaypointScaffoldNode) {
        node.updateWaypointContentDefinition(waypointScaffoldDefinition)
    }

    override fun InspectorInfo.inspectableProperties() {}
}
