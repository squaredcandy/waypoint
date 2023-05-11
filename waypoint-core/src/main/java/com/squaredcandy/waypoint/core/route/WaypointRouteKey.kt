package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier

object WaypointRouteKey {
    /**
     * Some predefined route keys for convenience. Feel free to provide your own.
     */
    val main: Identifier<WaypointRouteKey> = Identifier("main")
    val side: Identifier<WaypointRouteKey> = Identifier("side")
    val child: Identifier<WaypointRouteKey> = Identifier("side")
}
