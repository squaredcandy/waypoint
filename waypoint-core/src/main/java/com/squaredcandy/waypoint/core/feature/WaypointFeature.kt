package com.squaredcandy.waypoint.core.feature

import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition
import java.io.Serializable

sealed interface WaypointFeature : Serializable {
    fun getContent(): WaypointContent
    fun overrideTransition(): WaypointTransition? = null
}

/**
 * Waypoint feature that has content that takes up an entire screen
 */
interface MainWaypointFeature : WaypointFeature

/**
 * Waypoint feature that has content that overlays on top of the screen
 */
interface SideWaypointFeature : WaypointFeature
