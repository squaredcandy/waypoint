package com.squaredcandy.waypoint.core.feature.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import com.squaredcandy.waypoint.core.Waypoint

object MaterialReverseSharedAxisXScreenTransition : BacktrackWaypointTransition {
    override fun AnimatedContentTransitionScope<Waypoint>.transition(): ContentTransform {
        return slideInHorizontally { -(it * 0.1f).toInt() } + fadeIn(tween(150)) togetherWith
                slideOutHorizontally { (it * 0.2f).toInt() } + fadeOut(tween(150))
    }

    override fun navigateWaypointTransition(): WaypointTransition = MaterialSharedAxisXScreenTransition
}
