package com.squaredcandy.waypoint.core.contentV2

import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties

internal class WaypointContentNode(
    waypointContentDefinition: WaypointContentDefinition,
) : ModifierLocalModifierNode, SemanticsModifierNode, DelegatingNode() {
    private var waypointContentDefinitionDelegate = delegate(waypointContentDefinition)

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointContentDefinition to waypointContentDefinitionDelegate
        )

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[SemanticsProperties.WaypointContentDefinitionSemanticKey] = waypointContentDefinitionDelegate
    }

    fun updateWaypointContentDefinition(newWaypointContentDefinition: WaypointContentDefinition) {
        undelegate(waypointContentDefinitionDelegate)
        waypointContentDefinitionDelegate = delegate(newWaypointContentDefinition)
    }
}
