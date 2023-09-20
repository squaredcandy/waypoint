package com.squaredcandy.waypoint.core.route

import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.content.EmptyWaypointContent
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointFeature
import com.squaredcandy.waypoint.core.holder.DefaultWaypointHolder
import com.squaredcandy.waypoint.core.tags.OverlayWaypointTag
import kotlinx.coroutines.test.runTest
import org.junit.Test

class OverlayWaypointRouteTest {
    @Test
    fun `GIVEN empty waypoint holder THEN waypoint list is empty and can backtrack`() = runTest {
        val waypointHolder = DefaultWaypointHolder(mutableListOf())
        val waypointRoute = OverlayWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.waypointList).isEmpty()
    }

    @Test
    fun `GIVEN a non empty waypoint holder with main waypoint feature THEN waypoint list is empty and can backtrack`() = runTest {
        val waypoint = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint))
        val waypointRoute = OverlayWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.waypointList).isEmpty()
    }

    @Test
    fun `GIVEN a waypoint holder with a main and overlay waypoint feature THEN waypoint list is not empty and can backtrack`() = runTest {
        val waypointFeature = object : WaypointFeature {
            override fun getContent(): WaypointContent = EmptyWaypointContent
        }

        val waypoint1 = Waypoint()
        val waypoint2 = Waypoint(feature = waypointFeature, tags = setOf(OverlayWaypointTag))
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint1, waypoint2))
        val waypointRoute = OverlayWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.waypointList).isNotEmpty()
    }
}
