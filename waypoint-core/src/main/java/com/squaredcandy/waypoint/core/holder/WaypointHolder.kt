package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.Stable
import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList

val ModifierLocalWaypointHolder = modifierLocalOf<WaypointHolder?> { null }

@Stable
interface WaypointHolder {
    val parent: WaypointHolder?

    val waypointList: ImmutableList<Waypoint>
    val lastNavigationType: WaypointNavigationType?
}
