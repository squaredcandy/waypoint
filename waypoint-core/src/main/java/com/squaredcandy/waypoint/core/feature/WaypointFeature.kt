package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition
import java.io.Serializable

interface WaypointFeature : Serializable {
    fun getContent(): WaypointContent
    fun overrideTransition(): WaypointTransition? = null
}
