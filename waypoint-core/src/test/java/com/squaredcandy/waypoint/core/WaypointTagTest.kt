package com.squaredcandy.waypoint.core

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.holder.DefaultWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.route.WaypointRoute
import kotlinx.collections.immutable.toImmutableList
import org.junit.Test

class WaypointTagTest {
    private val testWaypointTag = WaypointTag("test_tag")

    @Test
    fun `GIVEN two tags with the same name WHEN compared THEN they should be equal`() {
        val compareTestTag = WaypointTag("test_tag")
        Truth.assertThat(compareTestTag).isEqualTo(testWaypointTag)
    }

    @Test
    fun `GIVEN two tags with the different names WHEN compared THEN they should not be equal`() {
        val compareTestTag = WaypointTag("test_tag2")
        Truth.assertThat(compareTestTag).isNotEqualTo(testWaypointTag)
    }

    @Test
    fun `GIVEN waypoint list WHEN getting only waypoints with tag THEN waypoints without tag are filtered out`() {
        val waypointHolder = DefaultWaypointHolder(
            mutableListOf(
                Waypoint(tags = setOf(testWaypointTag)),
                Waypoint(tags = setOf()),
                Waypoint(tags = setOf(testWaypointTag)),
                Waypoint(tags = setOf()),
            )
        )
        val waypointRoute = TestWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.waypointList).hasSize(2)
    }

    inner class TestWaypointRoute(private val waypointHolder: WaypointHolder) : WaypointRoute<TestWaypointRoute> {
        override val key: Identifier<TestWaypointRoute> = Identifier("test")

        override val waypointList by derivedStateOf {
            waypointHolder.waypointList.filter { it.tags.contains(WaypointTag("test_tag")) }
                .toImmutableList()
        }
        override val canBacktrack: Boolean = true
    }
}
