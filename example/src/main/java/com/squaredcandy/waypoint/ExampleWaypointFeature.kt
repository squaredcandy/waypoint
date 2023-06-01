package com.squaredcandy.waypoint

import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.transition.MaterialSharedAxisZScreenTransition
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition

object ExampleWaypointFeature : MainWaypointFeature {
    override fun getContent(): WaypointContent = ExampleWaypointContent()
    override fun overrideTransition(): WaypointTransition = MaterialSharedAxisZScreenTransition
}
