package com.squaredcandy.waypoint.core.content

import androidx.compose.ui.modifier.ModifierLocal

sealed interface WaypointContentScope {
    val <T> ModifierLocal<T>.currentOrNull: T?
}
