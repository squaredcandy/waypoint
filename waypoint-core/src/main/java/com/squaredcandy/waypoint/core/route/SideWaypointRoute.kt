package com.squaredcandy.waypoint.core.route

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import kotlinx.collections.immutable.ImmutableList

class SideWaypointRoute(waypointHolder: WaypointHolder) : WaypointRoute<SideWaypointRoute> {
    override val waypointList: ImmutableList<Waypoint> by derivedStateOf {
        waypointHolder.waypointList.subList(
            fromIndex = waypointHolder.waypointList
                .indexOfLast { waypoint -> waypoint.feature is MainWaypointFeature } + 1,
            toIndex = waypointHolder.waypointList.size,
        )
    }

    override val key: Identifier<SideWaypointRoute> = SideWaypointRoute.key

    override val canBacktrack: Boolean = true

    companion object {
        val key: Identifier<SideWaypointRoute> = Identifier("side")
    }
}
