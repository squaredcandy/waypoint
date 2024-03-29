package com.squaredcandy.waypoint.bottom_nav

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.WaypointTag
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.route.WaypointRoute
import com.squaredcandy.waypoint.core.route.WaypointTransitionSpecType
import com.squaredcandy.waypoint.core.route.lifecycle.WaypointRouteLifecycleProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow

class BottomNavigationWaypointRoute(
    private val waypointHolder: WaypointHolder,
) : WaypointRoute<BottomNavigationWaypointRoute>, WaypointRouteLifecycleProvider {
    override val waypointList: ImmutableList<Waypoint> by derivedStateOf {
        waypointHolder.waypointList.filter { waypoint ->
            waypoint.tags.contains(BottomNavigationWaypointTag)
        }
            .toImmutableList()
    }

    private val lastNavigationType: WaypointNavigationType?
        get() = waypointHolder.lastNavigationType

    override val key: Identifier<BottomNavigationWaypointRoute> = Companion.key

    override val waypointListFlow: Flow<List<Waypoint>> = snapshotFlow { waypointList }

    override val canBacktrack: Boolean by derivedStateOf {
        waypointHolder.waypointList
            .filter { waypoint -> waypoint.tags.contains(BottomNavigationWaypointTag) }
            .size > 1
    }

    fun getWaypointTransitionSpecType(initialWaypoint: Waypoint): WaypointTransitionSpecType {
        return WaypointTransitionSpecType.of(
            isNavigate = lastNavigationType != WaypointNavigationType.Pop,
            isEnter = waypointHolder.waypointList.any { it.id == initialWaypoint.id },
        )
    }

    companion object {
        val key: Identifier<BottomNavigationWaypointRoute> = Identifier("bottom_navigation")
        val BottomNavigationWaypointTag = WaypointTag("bottom_navigation")
    }
}
