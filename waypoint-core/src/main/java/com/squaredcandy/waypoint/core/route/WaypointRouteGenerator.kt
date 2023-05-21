package com.squaredcandy.waypoint.core.route

import kotlinx.collections.immutable.ImmutableList

internal data class WaypointRouteGenerator(
    val generateWaypointRouteList: ImmutableList<GenerateWaypointRoute<*>>,
)
