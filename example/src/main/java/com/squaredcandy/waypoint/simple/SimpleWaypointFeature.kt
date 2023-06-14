package com.squaredcandy.waypoint.simple

import com.squaredcandy.waypoint.core.feature.MainWaypointFeature
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.transition.MaterialSharedAxisZScreenTransition
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition

object SimpleWaypointFeature : MainWaypointFeature {
    override fun getContent(): WaypointContent = SimpleWaypointContent()
    override fun overrideTransition(): WaypointTransition = MaterialSharedAxisZScreenTransition
}
