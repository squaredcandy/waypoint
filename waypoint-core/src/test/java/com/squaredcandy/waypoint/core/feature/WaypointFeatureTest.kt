package com.squaredcandy.waypoint.core.feature

import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.content.EmptyWaypointContent
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.transition.FadeWaypointTransition
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WaypointFeatureTest {
    @Test
    fun `GIVEN empty waypoint feature THEN override transition is null`() = runTest {
        val waypointFeature = object : WaypointFeature {
            override fun getContent(): WaypointContent = EmptyWaypointContent
        }

        Truth.assertThat(waypointFeature.overrideTransition()).isNull()
    }

    @Test
    fun `GIVEN waypoint feature with transition THEN override transition is not null`() = runTest {
        val waypointFeature = object : WaypointFeature {
            override fun getContent(): WaypointContent = EmptyWaypointContent
            override fun overrideTransition(): WaypointTransition = FadeWaypointTransition
        }

        Truth.assertThat(waypointFeature.overrideTransition()).isNotNull()
    }
}
