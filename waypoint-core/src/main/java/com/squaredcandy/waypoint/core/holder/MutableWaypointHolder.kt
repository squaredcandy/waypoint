package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.Stable
import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Waypoint

val ModifierLocalMutableWaypointHolder = modifierLocalOf<MutableWaypointHolder> { EmptyWaypointHolder }

@Stable
interface MutableWaypointHolder: WaypointHolder {
    override val parent: MutableWaypointHolder?

    fun <T> updateWaypointList(
        navigationType: WaypointNavigationType,
        update: MutableList<Waypoint>.() -> T,
    ): T
}
