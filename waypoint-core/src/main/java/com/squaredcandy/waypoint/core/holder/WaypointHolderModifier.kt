package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.squaredcandy.waypoint.core.Waypoint

/**
 * The backbone of the waypoint navigation system.
 *
 * This holds a list of [Waypoints][Waypoint] and provides a [ModifierLocalWaypointHolder] &
 * [ModifierLocalMutableWaypointHolder] to child nodes to read and write.
 */
fun Modifier.waypointHolder(
    initialWaypointList: List<Waypoint>,
): Modifier = composed {
    val waypointSnapshotStateList = rememberSaveable(
        saver = listSaver(
            save = { it },
            restore = { it.toMutableStateList() },
        ),
    ) {
        initialWaypointList.toMutableStateList()
    }

    WaypointHolderNodeElement(waypointSnapshotStateList)
}
