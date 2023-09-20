package com.squaredcandy.waypoint.simple

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointFeature
import com.squaredcandy.waypoint.core.feature.transition.MaterialSharedAxisZScreenTransition
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition

class SimpleWaypointFeature(private val transition: WaypointTransition = MaterialSharedAxisZScreenTransition) : WaypointFeature {
    override fun getContent(): WaypointContent = SimpleWaypointContent()
    override fun overrideTransition(): WaypointTransition = transition
}
