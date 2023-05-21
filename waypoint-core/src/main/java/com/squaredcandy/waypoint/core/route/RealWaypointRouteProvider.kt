package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap

internal class RealWaypointRouteProvider(
    private val waypointHolder: WaypointHolder,
    waypointRouteGenerator: WaypointRouteGenerator,
) : WaypointRouteProvider {
    private val waypointRouteMap: ImmutableMap<Identifier<out WaypointRoute<*>>, WaypointRoute<*>> =
        waypointRouteGenerator.generateWaypointRouteList
            .associate {
                val waypointRoute = it.generate(waypointHolder)
                waypointRoute.key to waypointRoute
            }
            .toImmutableMap()

    @Suppress("UNCHECKED_CAST")
    override fun <T : WaypointRoute<T>> getRoute(key: Identifier<T>): T = waypointRouteMap[key] as? T
        ?: throw IllegalArgumentException("Missing route for id $key")
}
