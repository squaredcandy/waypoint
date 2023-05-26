package com.squaredcandy.waypoint.core.feature.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import com.squaredcandy.waypoint.core.Waypoint

object FadeWaypointTransition : BidirectionalWaypointTransition {
    override fun AnimatedContentTransitionScope<Waypoint>.transition(): ContentTransform {
        return fadeIn() togetherWith fadeOut()
    }
}
