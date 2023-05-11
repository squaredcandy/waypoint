package com.squaredcandy.waypoint

import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.feature.WaypointContent

object ExampleWaypointFeature : MainWaypointFeature {
    override fun getContent(): WaypointContent = ExampleWaypointContent()
}
