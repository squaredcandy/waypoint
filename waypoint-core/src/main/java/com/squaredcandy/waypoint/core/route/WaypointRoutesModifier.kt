package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.Modifier
import com.squaredcandy.waypoint.core.holder.WaypointHolder

/**
 * Modifier that creates routes from a given [WaypointHolder]. Routes are used in child nodes to
 * present the relevant nodes to the screen.
 *
 * Multiple routes can be provided to allow different views with the same [WaypointHolder].
 *
 * Routes are exposed to child nodes via [ModifierLocalWaypointRouteProvider].
 */
fun Modifier.waypointRoutes(builder: WaypointRouteMapScope.() -> Unit): Modifier =
    this.then(WaypointRouteProviderNodeElement(WaypointRouteMapBuilder().apply(builder).build()))
