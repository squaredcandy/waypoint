package com.squaredcandy.waypoint.core.route.lifecycle

import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.coroutines.flow.Flow

interface WaypointRouteLifecycleProvider {
    val waypointListFlow: Flow<List<Waypoint>>
}
