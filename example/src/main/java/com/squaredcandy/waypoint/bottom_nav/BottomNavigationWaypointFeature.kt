package com.squaredcandy.waypoint.bottom_nav

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.MainWaypointFeature

object BottomNavigationWaypointFeature : MainWaypointFeature {
    override fun getContent(): WaypointContent = BottomNavigationWaypointContent
}
