package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Identifier

val ModifierLocalWaypointRouteProvider = modifierLocalOf<WaypointRouteProvider?> { null }

interface WaypointRouteProvider {
    fun <T : WaypointRoute<T>> getRoute(key: Identifier<T>): T
}
