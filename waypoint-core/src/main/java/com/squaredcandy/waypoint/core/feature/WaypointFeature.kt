package com.squaredcandy.waypoint.core.feature

import java.io.Serializable

sealed interface WaypointFeature : Serializable {
    fun getContent(): WaypointContent
}

/**
 * Waypoint feature that has content that takes up an entire screen
 */
fun interface MainWaypointFeature : WaypointFeature

/**
 * Waypoint feature that has content that overlays on top of the screen
 */
fun interface SideWaypointFeature : WaypointFeature
