package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.Stable
import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList

val ModifierLocalWaypointHolder = modifierLocalOf<WaypointHolder> { EmptyWaypointHolder }

@Stable
interface WaypointHolder {
    val parent: WaypointHolder?
    val isDefined: Boolean

    val waypointList: ImmutableList<Waypoint>
    val lastNavigationType: WaypointNavigationType?
}
