package com.squaredcandy.waypoint.core.feature.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import com.squaredcandy.waypoint.core.Waypoint

object MaterialSharedAxisZScreenTransition : NavigateWaypointTransition {
    override fun AnimatedContentTransitionScope<Waypoint>.transition(): ContentTransform {
        return fadeIn(tween(300)) + scaleIn(tween(300), 0.8f) togetherWith
                scaleOut(tween(300), 1.1f) + fadeOut(tween(150))
    }

    override fun backtrackWaypointTransition(): WaypointTransition = MaterialReverseSharedAxisZScreenTransition
}
