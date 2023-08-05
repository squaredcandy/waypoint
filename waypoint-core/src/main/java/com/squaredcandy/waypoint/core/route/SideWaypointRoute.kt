package com.squaredcandy.waypoint.core.route

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.route.lifecycle.WaypointRouteLifecycleProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class SideWaypointRoute(
    waypointHolder: WaypointHolder,
) : WaypointRoute<SideWaypointRoute>,
    WaypointRouteLifecycleProvider {
    override val waypointList: ImmutableList<Waypoint> by derivedStateOf {
        waypointHolder.waypointList.subList(
            fromIndex = waypointHolder.waypointList
                .indexOfLast { waypoint -> waypoint.feature is MainWaypointFeature } + 1,
            toIndex = waypointHolder.waypointList.size,
        )
    }

    override val key: Identifier<SideWaypointRoute> = SideWaypointRoute.key

    override val waypointListFlow: Flow<List<Waypoint>> = snapshotFlow { waypointList }

    override val canBacktrack: Boolean = true

    companion object {
        val key: Identifier<SideWaypointRoute> = Identifier("side")
    }
}
