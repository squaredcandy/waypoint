package com.squaredcandy.waypoint.core.semantics

import androidx.compose.ui.semantics.SemanticsPropertyKey
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.scaffold.WaypointScaffoldDefinition
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.lifecycle.WaypointLifecycleOwner
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider

internal object SemanticsProperties {
    val WaypointHolderSemanticsKey = SemanticsPropertyKey<MutableWaypointHolder>("Mutable Waypoint Holder")
    val WaypointActionProviderSemanticsKey = SemanticsPropertyKey<WaypointActionProvider>("Waypoint Action Provider")
    val WaypointRouteProviderSemanticsKey = SemanticsPropertyKey<WaypointRouteProvider?>("Waypoint Route Provider")
    val WaypointLifecycleOwnerSemanticKey = SemanticsPropertyKey<WaypointLifecycleOwner?>("Waypoint Lifecycle Owner")
    val WaypointScaffoldDefinitionSemanticKey = SemanticsPropertyKey<WaypointScaffoldDefinition?>("Waypoint Content Definition")
}
