package com.squaredcandy.waypoint.core.route

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.route.lifecycle.WaypointRouteLifecycleProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow

class MainWaypointRoute(
    private val waypointHolder: WaypointHolder
) : WaypointRoute<MainWaypointRoute>,
    WaypointRouteLifecycleProvider {
    val waypointList: ImmutableList<Waypoint> by derivedStateOf {
        listOfNotNull(
            waypointHolder.waypointList
                .lastOrNull { waypoint -> waypoint.feature is MainWaypointFeature },
        )
            .toImmutableList()
    }

    private val lastNavigationType: WaypointNavigationType?
        get() = waypointHolder.lastNavigationType

    override val key: Identifier<MainWaypointRoute> = MainWaypointRoute.key

    override val waypointListFlow: Flow<List<Waypoint>> = snapshotFlow { waypointList }

    val canBacktrack: Boolean by derivedStateOf {
        waypointHolder.waypointList
            .filter { waypoint -> waypoint.feature is MainWaypointFeature }
            .size > 1
    }

    fun getWaypointTransitionSpecType(initialWaypoint: Waypoint): WaypointTransitionSpecType {
        return WaypointTransitionSpecType.of(
            isNavigate = lastNavigationType != WaypointNavigationType.Pop,
            isEnter = waypointHolder.waypointList.any { it.id == initialWaypoint.id },
        )
    }

    companion object {
        val key: Identifier<MainWaypointRoute> = Identifier("main")
    }
}
