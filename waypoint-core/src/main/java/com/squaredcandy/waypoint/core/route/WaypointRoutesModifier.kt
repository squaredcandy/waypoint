package com.squaredcandy.waypoint.core.route

import androidx.compose.ui.Modifier

/**
 * Provides a waypoint route provider for consumers to use.
 *
 * The waypoint route provider allows consumers to create 'routes' based on the key they provide.
 */
fun Modifier.waypointRoutes(builder: WaypointRouteMapBuilder.() -> Unit): Modifier =
    this.then(WaypointRouteProviderNodeElement(WaypointRouteMapBuilder().apply(builder).build()))
