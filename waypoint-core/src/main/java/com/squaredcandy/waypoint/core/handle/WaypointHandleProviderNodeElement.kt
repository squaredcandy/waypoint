package com.squaredcandy.waypoint.core.handle

import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo

internal class WaypointHandleProviderNodeElement : ModifierNodeElement<WaypointHandleProviderNode>() {
    override fun create(): WaypointHandleProviderNode = WaypointHandleProviderNode()

    override fun update(node: WaypointHandleProviderNode) {}

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun InspectorInfo.inspectableProperties() {}
}
