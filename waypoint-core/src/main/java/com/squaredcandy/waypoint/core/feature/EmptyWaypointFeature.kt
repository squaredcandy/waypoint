package com.squaredcandy.waypoint.core.feature

import androidx.compose.runtime.Composable

/**
 * An empty waypoint feature for convenience.
 */
object EmptyWaypointFeature : MainWaypointFeature {
    @Suppress("ObjectLiteralToLambda")
    override fun getContent(): WaypointContent = object : WaypointContent {
        context(WaypointFeature)
        @Composable
        override fun Content() {
        }
    }
}
