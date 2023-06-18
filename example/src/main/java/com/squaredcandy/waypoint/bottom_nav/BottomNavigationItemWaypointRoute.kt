package com.squaredcandy.waypoint.bottom_nav

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.WaypointTag
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.route.WaypointRoute
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class BottomNavigationItemWaypointRoute(
    private val waypointHolder: WaypointHolder,
) : WaypointRoute<BottomNavigationItemWaypointRoute> {
    override val key: Identifier<BottomNavigationItemWaypointRoute> = Companion.key

    fun waypointListOfTag(waypointTag: WaypointTag): State<ImmutableList<Waypoint>> = derivedStateOf {
        waypointHolder.waypointList.filter { waypoint ->
            waypoint.tags.contains(waypointTag)
        }
            .toImmutableList()
    }

    companion object {
        val key: Identifier<BottomNavigationItemWaypointRoute> = Identifier("navigation_bar_item")
    }
}
