package com.squaredcandy.waypoint.core.action.actions

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction

data class BacktrackWaypointAction(val waypointId: Identifier<Waypoint>) : WaypointAction
