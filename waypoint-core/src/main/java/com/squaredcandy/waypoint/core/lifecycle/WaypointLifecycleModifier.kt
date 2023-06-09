package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.addHook
import com.squaredcandy.waypoint.core.action.waypointActions

fun Modifier.waypointLifecycle(
    overrideSaveableStateHolder: SaveableStateHolder? = null
): Modifier = composed {
    val savedStateHolder = overrideSaveableStateHolder ?: rememberSaveableStateHolder()
    val waypointLifecycleOwnerNode = remember(savedStateHolder) {
        { SimpleWaypointLifecycleOwnerNode(savedStateHolder) }
    }
    Modifier
        .then(WaypointLifecycleNodeElement(waypointLifecycleOwnerNode))
        .waypointActions(mergeParentActions = true) {
            addHook(
                preResolveHook = { _, _ ->
                },
                postResolveHook = { _, waypointAction ->
                    if (waypointAction is BacktrackWaypointAction) {
                        savedStateHolder.removeState(waypointAction.waypointId)
                    }
                },
            )
        }
}
