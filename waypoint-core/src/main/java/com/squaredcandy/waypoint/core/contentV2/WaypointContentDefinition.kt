package com.squaredcandy.waypoint.core.contentV2

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalOf

internal val ModifierLocalWaypointContentDefinition = modifierLocalOf<WaypointContentDefinition> { EmptyWaypointContentDefinition() }

abstract class WaypointContentDefinition : ModifierLocalModifierNode, Modifier.Node() {
    @Composable
    abstract fun Content()
}
