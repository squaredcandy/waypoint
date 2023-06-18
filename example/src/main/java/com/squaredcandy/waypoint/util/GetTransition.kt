package com.squaredcandy.waypoint.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition
import com.squaredcandy.waypoint.core.route.WaypointTransitionSpecType

internal fun AnimatedContentTransitionScope<Waypoint>.getTransition(
    waypointTransitionSpecType: WaypointTransitionSpecType,
    fallbackTransition: WaypointTransition,
): ContentTransform {
    val transition = when (waypointTransitionSpecType) {
        WaypointTransitionSpecType.NavigateEnter -> targetState.feature.overrideTransition()
            ?: fallbackTransition

        WaypointTransitionSpecType.NavigateExit -> targetState.feature.overrideTransition()
            ?.backtrackWaypointTransition()
            ?: fallbackTransition.backtrackWaypointTransition()

        WaypointTransitionSpecType.BacktrackEnter -> initialState.feature.overrideTransition()
            ?: fallbackTransition

        WaypointTransitionSpecType.BacktrackExit -> initialState.feature.overrideTransition()
            ?.backtrackWaypointTransition()
            ?: fallbackTransition.backtrackWaypointTransition()
    }
    return with (transition) {
        this@getTransition.transition()
    }
}
