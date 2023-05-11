package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toImmutableMap

@DslMarker annotation class WaypointRouteMapScope

@WaypointRouteMapScope
class WaypointRouteMapBuilder {
    private var waypointRouteMap: PersistentMap<Identifier<WaypointRouteKey>, GenerateWaypointRoute> =
        persistentHashMapOf()

    fun addRoute(key: Identifier<WaypointRouteKey>, generateWaypointRoute: GenerateWaypointRoute) {
        waypointRouteMap = waypointRouteMap.put(key, generateWaypointRoute)
    }

    fun build(): ImmutableMap<Identifier<WaypointRouteKey>, GenerateWaypointRoute> = waypointRouteMap.toImmutableMap()
}
