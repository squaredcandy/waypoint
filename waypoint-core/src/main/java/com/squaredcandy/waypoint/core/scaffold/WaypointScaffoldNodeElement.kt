package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointScaffoldNodeElement(
    val waypointScaffoldContent: WaypointScaffoldContent,
) : ModifierNodeElement<WaypointScaffoldNode>() {
    override fun create(): WaypointScaffoldNode = WaypointScaffoldNode(waypointScaffoldContent)

    override fun update(node: WaypointScaffoldNode) {
        node.updateWaypointScaffoldContent(waypointScaffoldContent)
    }

    override fun InspectorInfo.inspectableProperties() {}
}
