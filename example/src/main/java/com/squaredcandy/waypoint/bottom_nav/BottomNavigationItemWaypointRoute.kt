package com.squaredcandy.waypoint.bottom_nav

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
    val waypointList: ImmutableList<Waypoint> by derivedStateOf {
        waypointHolder.waypointList.filter { waypoint ->
            waypoint.tags.contains(BottomNavigationItemWaypointTag)
        }
            .toImmutableList()
    }

    override val key: Identifier<BottomNavigationItemWaypointRoute> = Companion.key

    companion object {
        val key: Identifier<BottomNavigationItemWaypointRoute> = Identifier("bottom_navigation_item")
        val BottomNavigationItemWaypointTag = WaypointTag("bottom_navigation_item")
    }
}
