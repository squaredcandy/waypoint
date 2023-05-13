package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import com.squaredcandy.waypoint.core.Waypoint

internal data class WaypointHolderNodeElement(
    val snapshotWaypointList: SnapshotStateList<Waypoint>,
) : ModifierNodeElement<WaypointHolderNode>() {
    override fun create(): WaypointHolderNode = WaypointHolderNode(snapshotWaypointList)

    override fun update(node: WaypointHolderNode) {
        node.mutableWaypointList = snapshotWaypointList
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "Waypoint Holder"
        snapshotWaypointList.forEachIndexed { index, waypoint ->
            properties["Waypoint $index"] = waypoint
        }
    }
}
