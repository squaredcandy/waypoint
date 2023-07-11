package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties

internal class WaypointScaffoldNode(
    waypointScaffoldDefinition: WaypointScaffoldDefinition,
) : ModifierLocalModifierNode, SemanticsModifierNode, DelegatingNode() {
    private var waypointContentDefinitionDelegate = delegate(waypointScaffoldDefinition)

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointScaffoldDefinition to waypointContentDefinitionDelegate
        )

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[SemanticsProperties.WaypointScaffoldDefinitionSemanticKey] = waypointContentDefinitionDelegate
    }

    fun updateWaypointContentDefinition(newWaypointScaffoldDefinition: WaypointScaffoldDefinition) {
        undelegate(waypointContentDefinitionDelegate)
        waypointContentDefinitionDelegate = delegate(newWaypointScaffoldDefinition)
    }
}
