package com.squaredcandy.waypoint.core.feature

import androidx.compose.runtime.Composable
import com.squaredcandy.waypoint.core.content.EmptyWaypointContent
import com.squaredcandy.waypoint.core.content.WaypointContent

/**
 * An empty waypoint feature for convenience.
 */
object EmptyWaypointFeature : MainWaypointFeature {
    override fun getContent(): WaypointContent = EmptyWaypointContent
}
