package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointLifecycleOwnerSemanticKey

class WaypointLifecycleNode(
    waypointLifecycleOwnerNode: () -> WaypointLifecycleOwnerNode,
) : ModifierLocalModifierNode, SemanticsModifierNode, DelegatingNode() {
    private var waypointLifecycleOwner = delegate(waypointLifecycleOwnerNode())

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointLifecycleOwner to waypointLifecycleOwner,
        )

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[WaypointLifecycleOwnerSemanticKey] = waypointLifecycleOwner
    }

    fun updateLifecycleOwner(newWaypointLifecycleOwnerNode: () -> WaypointLifecycleOwnerNode) {
        undelegate(waypointLifecycleOwner)
        waypointLifecycleOwner = delegate(newWaypointLifecycleOwnerNode())
    }
}
