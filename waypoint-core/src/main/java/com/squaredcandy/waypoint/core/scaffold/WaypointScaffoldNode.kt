package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties

internal class WaypointScaffoldNode(
    waypointScaffoldContent: WaypointScaffoldContent,
) : ModifierLocalModifierNode, SemanticsModifierNode, DelegatingNode() {
    private var waypointScaffoldContentDelegate = delegate(waypointScaffoldContent)

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointScaffoldContent to waypointScaffoldContentDelegate
        )

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[SemanticsProperties.WaypointScaffoldContentSemanticKey] = waypointScaffoldContentDelegate
    }

    fun updateWaypointScaffoldContent(newWaypointScaffoldContent: WaypointScaffoldContent) {
        undelegate(waypointScaffoldContentDelegate)
        waypointScaffoldContentDelegate = delegate(newWaypointScaffoldContent)
    }
}
