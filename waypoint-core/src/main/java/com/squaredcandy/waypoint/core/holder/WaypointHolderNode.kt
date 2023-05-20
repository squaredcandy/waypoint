package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsConfiguration
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties

// A modifier that holds a single list of waypoints
internal class WaypointHolderNode(
    var mutableWaypointList: SnapshotStateList<Waypoint>,
) : ModifierLocalNode, SemanticsModifierNode, Modifier.Node() {

    private val waypointHolder: MutableWaypointHolder by derivedStateOf {
        DefaultWaypointHolder(mutableWaypointList, parentWaypointHolder)
    }

    private val parentWaypointHolder: MutableWaypointHolder?
        get() = if (isAttached) ModifierLocalMutableWaypointHolder.current else null

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointHolder to waypointHolder,
            ModifierLocalMutableWaypointHolder to waypointHolder,
        )

    override val semanticsConfiguration: SemanticsConfiguration
        get() = SemanticsConfiguration()
            .apply {
                this[SemanticsProperties.WaypointHolderSemanticsKey] = waypointHolder
            }
}
