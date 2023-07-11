package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalOf

internal val ModifierLocalWaypointScaffoldDefinition = modifierLocalOf<WaypointScaffoldDefinition> { EmptyWaypointScaffoldDefinition() }

abstract class WaypointScaffoldDefinition : ModifierLocalModifierNode, Modifier.Node() {
    @Composable
    abstract fun Content()
}
