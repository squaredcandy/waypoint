package com.squaredcandy.waypoint.core.content

import androidx.compose.ui.modifier.ModifierLocal
import androidx.compose.ui.modifier.ModifierLocalReadScope

internal class RealWaypointContentScope(
    scope: ModifierLocalReadScope,
) : WaypointContentScope, ModifierLocalReadScope by scope {
    override val <T> ModifierLocal<T>.currentOrNull: T
        get() = this.current
}
