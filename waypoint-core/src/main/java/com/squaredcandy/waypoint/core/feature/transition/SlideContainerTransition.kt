package com.squaredcandy.waypoint.core.feature.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.togetherWith
import com.squaredcandy.waypoint.core.Waypoint

class SlideContainerTransition(
    private val slideDirection: AnimatedContentTransitionScope.SlideDirection,
) : BidirectionalWaypointTransition {
    override fun AnimatedContentTransitionScope<Waypoint>.transition(): ContentTransform {
        return slideIntoContainer(slideDirection) togetherWith slideOutOfContainer(slideDirection)
    }
}
