package com.squaredcandy.waypoint.core.handle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint

abstract class WaypointHandle(
    protected val waypointId: Identifier<Waypoint>,
    modifierLocalReadScope: ModifierLocalReadScope,
): ModifierLocalReadScope by modifierLocalReadScope

val ModifierLocalWaypointHandleProvider = modifierLocalOf<WaypointHandleProvider> { error("Missing WaypointHandleProvider") }
val LocalWaypointHandleProvider = compositionLocalOf<WaypointHandleProvider> { error("Missing WaypointHandleProvider") }
val LocalWaypoint = compositionLocalOf<Waypoint> { error("Missing Waypoint") }

@Composable
fun <T: WaypointHandle> rememberWaypointHandle(
    constructor: (Identifier<Waypoint>, ModifierLocalReadScope) -> T,
): T {
    val handle = LocalWaypointHandleProvider.current
    val waypoint = LocalWaypoint.current
    return remember(handle, waypoint, constructor) {
        handle.buildWaypointHandle(constructor, waypoint)
    }
}
