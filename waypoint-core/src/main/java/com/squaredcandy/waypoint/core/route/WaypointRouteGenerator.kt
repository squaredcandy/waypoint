package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier
import kotlinx.collections.immutable.ImmutableMap

internal data class WaypointRouteGenerator(
    val generateWaypointRouteMap: ImmutableMap<Identifier<WaypointRouteKey>, GenerateWaypointRoute>,
)
