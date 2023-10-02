package com.squaredcandy.waypoint.core.feature.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.runtime.Immutable
import com.squaredcandy.waypoint.core.Waypoint
import java.io.Serializable

@Immutable
sealed interface WaypointTransition : Serializable {
    fun AnimatedContentTransitionScope<Waypoint>.transition(): ContentTransform

    fun navigateWaypointTransition(): WaypointTransition
    fun backtrackWaypointTransition(): WaypointTransition
}
