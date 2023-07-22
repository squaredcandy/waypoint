package com.squaredcandy.waypoint.core.handle

import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.DelegatingNode

internal class WaypointHandleProviderNode : ModifierLocalModifierNode, DelegatingNode() {
    private val provider = delegate(RealWaypointHandleProvider())

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointHandleProvider to provider,
        )
}
