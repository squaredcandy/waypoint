package com.squaredcandy.waypoint.core.action.actions

import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction

data class NavigateWaypointAction(val waypoint: Waypoint) : WaypointAction
