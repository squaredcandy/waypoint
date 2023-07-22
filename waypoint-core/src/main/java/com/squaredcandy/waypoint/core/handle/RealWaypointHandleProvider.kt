package com.squaredcandy.waypoint.core.handle

import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.ModifierLocalReadScope
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint

internal class RealWaypointHandleProvider : WaypointHandleProvider, ModifierLocalModifierNode, Modifier.Node() {
    override fun <T: WaypointHandle> buildWaypointHandle(
        constructor: (Identifier<Waypoint>, ModifierLocalReadScope) -> T,
        waypoint: Waypoint,
    ): T {
        return constructor(waypoint.id, this)
    }
}
