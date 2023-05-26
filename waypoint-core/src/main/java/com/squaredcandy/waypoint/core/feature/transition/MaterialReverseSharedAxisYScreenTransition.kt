package com.squaredcandy.waypoint.core.feature.transition

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import com.squaredcandy.waypoint.core.Waypoint

object MaterialReverseSharedAxisYScreenTransition : BacktrackWaypointTransition {
    override fun AnimatedContentTransitionScope<Waypoint>.transition(): ContentTransform {
        return slideInVertically { -(it * 0.1f).toInt() } + fadeIn(tween(150)) togetherWith
                slideOutVertically { (it * 0.2f).toInt() } + fadeOut(tween(150))
    }

    override fun navigateWaypointTransition(): WaypointTransition = MaterialSharedAxisYScreenTransition
}
