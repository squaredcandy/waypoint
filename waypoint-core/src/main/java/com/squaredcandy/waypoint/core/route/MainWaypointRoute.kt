package com.squaredcandy.waypoint.core.route

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class MainWaypointRoute(waypointHolder: WaypointHolder) : WaypointRoute {
    override val waypointList: ImmutableList<Waypoint> by derivedStateOf {
        listOfNotNull(
            waypointHolder.waypointList
                .lastOrNull { waypoint -> waypoint.feature is MainWaypointFeature },
        )
            .toImmutableList()
    }
}
