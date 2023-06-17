package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.ModifierLocalWaypointActionProvider
import com.squaredcandy.waypoint.core.feature.RealWaypointContext
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder

class SimpleWaypointLifecycleOwnerNode(
    private val savedStateHolder: SaveableStateHolder,
) : ModifierLocalModifierNode, WaypointLifecycleOwnerNode() {
    @Composable
    override fun WithLifecycle(
        waypoint: Waypoint,
        block: @Composable WaypointContext.() -> Unit,
    ) {
        val mutableWaypointHolder = ModifierLocalMutableWaypointHolder.current
        val waypointActionProvider = ModifierLocalWaypointActionProvider.current
        val waypointContext = remember(
            key1 = waypoint,
            key2 = mutableWaypointHolder,
            key3 = waypointActionProvider,
        ) {
            RealWaypointContext(
                waypoint = waypoint,
                mutableWaypointHolder = mutableWaypointHolder,
                waypointActionProvider = waypointActionProvider,
            )
        }
        savedStateHolder.SaveableStateProvider(key = waypointContext.waypointId) {
            block(waypointContext)
        }
    }
}
