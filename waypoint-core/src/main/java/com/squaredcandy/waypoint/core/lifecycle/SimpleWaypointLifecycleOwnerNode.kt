package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
        val mutableWaypointHolder =
            rememberUpdatedState(newValue = ModifierLocalMutableWaypointHolder.current)
        val waypointActionProvider =
            rememberUpdatedState(newValue = ModifierLocalWaypointActionProvider.current)
        val waypointContext by remember {
            derivedStateOf {
                RealWaypointContext(
                    waypoint = waypoint,
                    mutableWaypointHolderState = mutableWaypointHolder,
                    waypointActionProviderState = waypointActionProvider,
                )
            }
        }
        savedStateHolder.SaveableStateProvider(key = waypoint.id) {
            block(waypointContext)
        }
    }
}
