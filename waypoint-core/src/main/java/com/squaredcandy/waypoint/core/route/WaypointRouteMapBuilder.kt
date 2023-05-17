package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toImmutableMap

class WaypointRouteMapBuilder : WaypointRouteMapScope {
    private var generateWaypointRouteMap = persistentHashMapOf<Identifier<WaypointRouteKey>, GenerateWaypointRoute>()

    override fun addRoute(
        key: Identifier<WaypointRouteKey>,
        generateWaypointRoute: GenerateWaypointRoute,
    ) {
        generateWaypointRouteMap = generateWaypointRouteMap.put(key, generateWaypointRoute)
    }

    fun build(): WaypointRouteGenerator = WaypointRouteGenerator(
        generateWaypointRouteMap = generateWaypointRouteMap.toImmutableMap(),
    )
}
