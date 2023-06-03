package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap

internal class WaypointActionSetSource(
    private val waypointActionSetList: SnapshotStateList<WaypointActionSet> = mutableStateListOf(),
): MutableList<WaypointActionSet> by waypointActionSetList {
    val waypointActionSet by derivedStateOf {
        val setList = waypointActionSetList
        if (setList.isEmpty()) {
            WaypointActionSet(persistentMapOf(), persistentListOf())
        } else {
            setList.reduce { acc, waypointActionSet -> acc.plus(waypointActionSet) }
        }
    }
}

private operator fun WaypointActionSet.plus(otherWaypointActionSet: WaypointActionSet): WaypointActionSet {
    return WaypointActionSet(
        resolvers = this.resolvers.toPersistentMap().putAll(otherWaypointActionSet.resolvers),
        hooks = this.hooks.toPersistentList().addAll(otherWaypointActionSet.hooks),
    )
}
