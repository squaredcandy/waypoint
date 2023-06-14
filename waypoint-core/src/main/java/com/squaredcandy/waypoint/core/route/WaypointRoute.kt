package com.squaredcandy.waypoint.core.route

import com.squaredcandy.waypoint.core.Identifier

interface WaypointRoute<T> {
    val key: Identifier<T>
}
