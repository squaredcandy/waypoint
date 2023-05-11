package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * Modifier that provides a set of actions that be taken on a waypoint as directly modifying the waypoints could lead to unexpected results.
 *
 * This could also be useful for overriding or disallowing certain actions.
 */
fun Modifier.waypointActions(
    builder: WaypointActionMapBuilder.() -> Unit,
): Modifier = composed {
    val map = remember(builder) {
        buildWaypointActions(builder)
    }

    WaypointActionNodeElement(map)
}
