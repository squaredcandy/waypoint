package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

class RealWaypointRouteProvider(
    private val waypointHolder: WaypointHolder,
    waypointRouteGenerator: WaypointRouteGenerator,
) : WaypointRouteProvider {
    private val waypointRouteMap: ImmutableMap<Identifier<WaypointRouteKey>, WaypointRoute> =
        waypointRouteGenerator.generateWaypointRouteMap
            .mapValues { it.value.generate(waypointHolder) }
            .toImmutableMap()

    override fun getRoute(key: Identifier<WaypointRouteKey>): WaypointRoute = waypointRouteMap[key]
        ?: throw IllegalArgumentException("Missing route for id $key")
}
