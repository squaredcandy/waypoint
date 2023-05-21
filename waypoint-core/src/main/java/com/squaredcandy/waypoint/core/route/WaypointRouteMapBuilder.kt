package com.squaredcandy.waypoint.core.route

import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

class WaypointRouteMapBuilder : WaypointRouteMapScope {
    private var generateWaypointRouteList = persistentListOf<GenerateWaypointRoute<*>>()

    override fun <T : WaypointRoute<T>> addRoute(
        generateWaypointRoute: GenerateWaypointRoute<T>
    ) {
        generateWaypointRouteList = generateWaypointRouteList.add(generateWaypointRoute)
    }

    internal fun build(): WaypointRouteGenerator = WaypointRouteGenerator(
        generateWaypointRouteList = generateWaypointRouteList.toImmutableList(),
    )
}
