package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.ModifierLocalReadScope
import com.squaredcandy.waypoint.core.holder.WaypointHolder

/**
 * Modifier that creates a set of actions that can be used in child nodes to modify the
 * [WaypointHolder] in a standardised manner instead of directly modifying the [WaypointHolder].
 *
 * The actions are exposed to child nodes via [ModifierLocalWaypointActionProvider].
 */
fun Modifier.waypointActions(
    mergeParentActions: Boolean = true,
    builder: context(ModifierLocalReadScope) WaypointActionMapBuilder.() -> Unit,
): Modifier = composed {
    val builderState = rememberUpdatedState(newValue = builder)

    WaypointActionNodeElement(mergeParentActions, builderState)
}
