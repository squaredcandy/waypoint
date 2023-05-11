package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.squaredcandy.waypoint.core.Waypoint

/**
 * Modifier that provides a [WaypointHolder] for nodes underneath this modifier to modify.
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
