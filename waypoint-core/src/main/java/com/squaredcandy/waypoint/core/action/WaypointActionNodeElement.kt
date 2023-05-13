package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.State
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal data class WaypointActionNodeElement(
    val waypointActionMapBuilderState: State<context(ModifierLocalReadScope) WaypointActionMapBuilder.() -> Unit>,
) : ModifierNodeElement<WaypointActionNode>() {
    override fun create(): WaypointActionNode = WaypointActionNode(waypointActionMapBuilderState)

    override fun update(node: WaypointActionNode): WaypointActionNode {
        node.waypointActionMapBuilderState = waypointActionMapBuilderState
        return node
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Action Node"
    }
}
