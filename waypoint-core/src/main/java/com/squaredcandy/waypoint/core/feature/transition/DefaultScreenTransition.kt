package com.squaredcandy.waypoint.core.feature.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.runtime.Immutable
import com.squaredcandy.waypoint.core.Waypoint

@Immutable
object DefaultScreenTransition : BidirectionalWaypointTransition {
    override fun AnimatedContentTransitionScope<Waypoint>.transition(): ContentTransform {
        return fadeIn(tween(220, 90)) +
                scaleIn(tween(220, 90), 0.92f) togetherWith
                fadeOut(tween(90))
    }

    private fun readResolve(): Any = DefaultScreenTransition
}
