package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentMap

internal class WaypointActionSetSource(
    private val waypointActionSetMap: SnapshotStateMap<Modifier.Node, WaypointActionSet> = mutableStateMapOf(),
): MutableMap<Modifier.Node, WaypointActionSet> by waypointActionSetMap {
    val waypointActionMap by derivedStateOf {
        val sets = waypointActionSetMap.values
        if (sets.isEmpty()) {
            WaypointActionSet(persistentMapOf(), persistentListOf())
        } else {
            sets.reduce { acc, waypointActionSet -> acc.plus(waypointActionSet) }
        }
    }
}

private operator fun WaypointActionSet?.plus(otherWaypointActionSet: WaypointActionSet): WaypointActionSet {
    return if (this != null) {
        WaypointActionSet(
            resolvers = this.resolvers.toPersistentMap().putAll(otherWaypointActionSet.resolvers),
            hooks = this.hooks.plus(otherWaypointActionSet.hooks).toImmutableList(),
        )
    } else {
        otherWaypointActionSet
    }
}
