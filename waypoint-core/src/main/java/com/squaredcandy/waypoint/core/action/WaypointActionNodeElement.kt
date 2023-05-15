package com.squaredcandy.waypoint.core.action

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointActionNodeElement(
    val mergeParentActions: Boolean,
    val waypointActionMap: WaypointActionMap,
) : ModifierNodeElement<WaypointActionNode>() {
    override fun create(): WaypointActionNode = WaypointActionNode(
        mergeParentActions,
        waypointActionMap,
    )

    override fun update(node: WaypointActionNode) {
        node.mergeParentActions = mergeParentActions
        node.waypointActionMap = waypointActionMap
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Action Node"
    }
}
