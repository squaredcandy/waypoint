package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

data class WaypointLifecycleNodeElement(
    val waypointLifecycleOwnerNode: () -> WaypointLifecycleOwnerNode,
) : ModifierNodeElement<WaypointLifecycleNode>() {
    override fun create(): WaypointLifecycleNode = WaypointLifecycleNode(waypointLifecycleOwnerNode)

    override fun update(node: WaypointLifecycleNode) {
        node.updateLifecycleOwner(waypointLifecycleOwnerNode)
    }

    override fun InspectorInfo.inspectableProperties() {}
}
