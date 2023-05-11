package com.squaredcandy.waypoint.core.feature

import java.io.Serializable

sealed interface WaypointFeature : Serializable {
    fun getContent(): WaypointContent
}

fun interface MainWaypointFeature : WaypointFeature

fun interface SideWaypointFeature : WaypointFeature
