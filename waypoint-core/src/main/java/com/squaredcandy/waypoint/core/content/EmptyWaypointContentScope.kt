package com.squaredcandy.waypoint.core.content

import androidx.compose.ui.modifier.ModifierLocal

internal object EmptyWaypointContentScope : WaypointContentScope {
    override val <T> ModifierLocal<T>.currentOrNull: T?
        get() = null
}
