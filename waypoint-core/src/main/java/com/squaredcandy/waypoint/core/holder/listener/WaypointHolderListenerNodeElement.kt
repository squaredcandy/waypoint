package com.squaredcandy.waypoint.core.holder.listener

import android.annotation.SuppressLint
import androidx.compose.ui.node.ModifierNodeElement

@SuppressLint("ModifierNodeInspectableProperties")
@ExperimentalWaypointHolderListenerModifier
internal class WaypointHolderListenerNodeElement : ModifierNodeElement<WaypointHolderListenerNode>() {
    override fun create(): WaypointHolderListenerNode = WaypointHolderListenerNode()

    override fun update(node: WaypointHolderListenerNode) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
