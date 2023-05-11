package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Identifier

val ModifierLocalWaypointRouteProvider = modifierLocalOf<WaypointRouteProvider> { EmptyWaypointRouteProvider() }

fun interface WaypointRouteProvider {
    fun getRoute(key: Identifier<WaypointRouteKey>): WaypointRoute
}
