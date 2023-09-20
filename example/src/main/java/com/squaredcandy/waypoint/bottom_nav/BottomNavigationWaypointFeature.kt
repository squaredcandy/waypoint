package com.squaredcandy.waypoint.bottom_nav

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointFeature

object BottomNavigationWaypointFeature : WaypointFeature {
    override fun getContent(): WaypointContent = BottomNavigationWaypointContent
}
