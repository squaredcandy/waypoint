package com.squaredcandy.waypoint.core

import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold

data class WaypointChangeList(
    val added: List<Waypoint>,
    val removed: List<Waypoint>,
)

/**
 * Converts a compose state backed List<Waypoint> into a [WaypointChangeList] flow allowing us
 * get a diff of the waypoints that has been changed.
 */
fun List<Waypoint>.toWaypointChangeListFlow(): Flow<WaypointChangeList> = snapshotFlow { this }
    .runningFold<List<Waypoint>, Pair<List<Waypoint>, List<Waypoint>>>(
        emptyList<Waypoint>() to emptyList(),
    ) { last, current -> last.second to current }
    .drop(1) // Drop first iteration
    .map { (old, new) ->
        val addedItemList = new.minus(old.toHashSet())
        val removedItemList = old.minus(new.toHashSet())
        WaypointChangeList(
            added = addedItemList,
            removed = removedItemList,
        )
    }
