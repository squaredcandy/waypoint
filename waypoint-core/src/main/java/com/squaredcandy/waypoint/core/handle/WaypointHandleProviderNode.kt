package com.squaredcandy.waypoint.core.handle

import androidx.compose.ui.modifier.ModifierLocalMap
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.SemanticsModifierNode
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties

internal class WaypointHandleProviderNode : ModifierLocalModifierNode, SemanticsModifierNode, DelegatingNode() {
    private val providerDelegate = delegate(RealWaypointHandleProvider())

    override val providedValues: ModifierLocalMap
        get() = modifierLocalMapOf(
            ModifierLocalWaypointHandleProvider to providerDelegate,
        )

    override fun SemanticsPropertyReceiver.applySemantics() {
        this[SemanticsProperties.WaypointHandleProviderSemanticKey] = providerDelegate
    }
}
