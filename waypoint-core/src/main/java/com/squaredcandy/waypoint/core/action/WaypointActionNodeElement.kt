package com.squaredcandy.waypoint.core.action

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointActionNodeElement(
    val mergeParentActions: Boolean,
    val waypointActionSet: WaypointActionSet,
) : ModifierNodeElement<WaypointActionNode>() {
    override fun create(): WaypointActionNode = WaypointActionNode(
        mergeParentActions,
        waypointActionSet,
    )

    override fun update(node: WaypointActionNode) {
        node.updateWaypointAction(mergeParentActions, waypointActionSet)
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Action Node"
    }
}
