package com.squaredcandy.waypoint.core.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.modifier.ModifierLocal
import androidx.compose.ui.modifier.ModifierLocalReadScope

sealed interface WaypointContentScope {
    val <T> ModifierLocal<T>.currentOrNull: T?
}

object EmptyWaypointContentScope : WaypointContentScope {
    override val <T> ModifierLocal<T>.currentOrNull: T?
        get() = null
}

internal class RealWaypointContentScope(
    scope: ModifierLocalReadScope,
) : WaypointContentScope, ModifierLocalReadScope by scope {
    override val <T> ModifierLocal<T>.currentOrNull: T
        get() = this.current
}

@Composable
fun <T> WaypointContentScope.rememberModifierLocalState(modifierLocal: ModifierLocal<T>): State<T?> = remember(this) {
    derivedStateOf { modifierLocal.currentOrNull }
}
