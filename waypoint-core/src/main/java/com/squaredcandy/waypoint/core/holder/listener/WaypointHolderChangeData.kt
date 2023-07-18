package com.squaredcandy.waypoint.core.holder.listener

import com.squaredcandy.waypoint.core.Waypoint

data class WaypointHolderChangeData(
    val added: List<Waypoint>,
    val removed: List<Waypoint>,
)
