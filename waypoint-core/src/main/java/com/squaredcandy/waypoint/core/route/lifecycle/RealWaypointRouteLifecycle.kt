package com.squaredcandy.waypoint.core.route.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.SaveableStateHolder
import androidx.compose.runtime.snapshotFlow
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.handle.LocalWaypoint
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.toWaypointRouteChangeList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
internal class RealWaypointRouteLifecycle(
    private val saveableStateHolder: SaveableStateHolder,
    waypointHolder: WaypointHolder,
    waypointRouteLifecycleProvider: WaypointRouteLifecycleProvider,
    coroutineScope: CoroutineScope,
) : WaypointRouteLifecycle {

    init {
        coroutineScope.launch {
            waypointRouteLifecycleProvider.waypointListFlow
                .toWaypointRouteChangeList(snapshotFlow { waypointHolder.waypointList })
                .flatMapMerge { it.removed.asFlow() }
                .onEach { waypoint -> saveableStateHolder.removeState(waypoint.id) }
                .collect()
        }
    }

    @Composable
    override fun WithLifecycle(waypoint: Waypoint, content: @Composable () -> Unit) {
        saveableStateHolder.SaveableStateProvider(waypoint.id) {
            CompositionLocalProvider(LocalWaypoint provides waypoint, content = content)
        }
    }
}
