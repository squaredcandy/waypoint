package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.ModifierLocalReadScope

/**
 * Modifier that provides a set of actions that be taken on a waypoint as directly modifying the waypoints could lead to unexpected results.
 *
 * This could also be useful for overriding or disallowing certain actions.
 */
fun Modifier.waypointActions(
    builder: context(ModifierLocalReadScope) WaypointActionMapBuilder.() -> Unit,
): Modifier = composed {
    val builderState = rememberUpdatedState(newValue = builder)

    WaypointActionNodeElement(builderState)
}
