package com.squaredcandy.waypoint.core.holder.listener

import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.DelegatingNode


@ExperimentalWaypointHolderListenerModifier
internal class WaypointHolderListenerNode : ModifierLocalModifierNode, DelegatingNode() {

    private val manager = delegate(RealWaypointHolderListenerManager())

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointHolderListenerManager to manager,
        )
}
