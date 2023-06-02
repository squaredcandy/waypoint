package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap

internal class WaypointActionSetSource(
    // Using SnapshotStateList instead of SnapshotStateMap as it preserves insertion order. Not the best solution as we lose the ergonomics of a map.
    private val waypointActionSetList: SnapshotStateList<Pair<Modifier.Node, WaypointActionSet>> = mutableStateListOf(),
): MutableList<Pair<Modifier.Node, WaypointActionSet>> by waypointActionSetList {
    val waypointActionMap by derivedStateOf {
        val sets = waypointActionSetList.map(Pair<Modifier.Node, WaypointActionSet>::second)
        if (sets.isEmpty()) {
            WaypointActionSet(persistentMapOf(), persistentListOf())
        } else {
            sets.reduce { acc, waypointActionSet -> acc.plus(waypointActionSet) }
        }
    }
}

private operator fun WaypointActionSet.plus(otherWaypointActionSet: WaypointActionSet): WaypointActionSet {
    return WaypointActionSet(
        resolvers = this.resolvers.toPersistentMap().putAll(otherWaypointActionSet.resolvers),
        hooks = this.hooks.toPersistentList().addAll(otherWaypointActionSet.hooks),
    )
}
