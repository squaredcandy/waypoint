package com.squaredcandy.waypoint.core.route

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class MainWaypointRoute(private val waypointHolder: WaypointHolder) : WaypointRoute<MainWaypointRoute> {
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
