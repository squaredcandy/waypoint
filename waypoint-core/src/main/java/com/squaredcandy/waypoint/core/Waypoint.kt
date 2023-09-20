package com.squaredcandy.waypoint.core

import androidx.compose.runtime.Stable
import com.squaredcandy.waypoint.core.feature.EmptyWaypointFeature
import com.squaredcandy.waypoint.core.feature.WaypointFeature
import com.squaredcandy.waypoint.core.tags.MainWaypointTag
import java.io.Serializable

@Stable
data class Waypoint(
    val id: Identifier<Waypoint> = randomIdentifier(),
    val feature: WaypointFeature = EmptyWaypointFeature,
    val tags: Set<WaypointTag> = setOf(MainWaypointTag),
) : Serializable
