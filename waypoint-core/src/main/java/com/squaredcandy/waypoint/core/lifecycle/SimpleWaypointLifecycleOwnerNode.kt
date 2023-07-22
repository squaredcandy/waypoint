package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import com.squaredcandy.waypoint.core.Waypoint

class SimpleWaypointLifecycleOwnerNode(
    private val savedStateHolder: SaveableStateHolder,
) : ModifierLocalModifierNode, WaypointLifecycleOwnerNode() {
    @Composable
    override fun WithLifecycle(
        waypoint: Waypoint,
        block: @Composable () -> Unit,
    ) {
        savedStateHolder.SaveableStateProvider(key = waypoint.id) {
            block()
        }
    }
}
