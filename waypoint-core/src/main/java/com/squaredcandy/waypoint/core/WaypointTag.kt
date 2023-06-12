package com.squaredcandy.waypoint.core

import java.io.Serializable

@Suppress("unused")
@JvmInline
value class WaypointTag(val tag: String): Serializable by tag
