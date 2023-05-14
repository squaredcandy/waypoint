package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.State
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointActionNodeElement(
    val mergeParentActions: Boolean,
    val waypointActionMapBuilderState: State<context(ModifierLocalReadScope) WaypointActionMapBuilder.() -> Unit>,
) : ModifierNodeElement<WaypointActionNode>() {
    override fun create(): WaypointActionNode = WaypointActionNode(
        mergeParentActions,
        waypointActionMapBuilderState,
    )

    override fun update(node: WaypointActionNode) {
        node.mergeParentActions = mergeParentActions
        node.waypointActionMapBuilderState = waypointActionMapBuilderState
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Action Node"
    }
}
