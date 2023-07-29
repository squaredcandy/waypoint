package com.squaredcandy.waypoint.core

import app.cash.turbine.test
import com.google.common.truth.Truth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WaypointRouteChangeListTest {
    @Test
    fun `GIVEN flow of waypoint route change list with nothing WHEN new waypoint is added THEN new waypoint is active`() = runTest {
        val waypoint = Waypoint()
        val waypointRouteFlow = MutableStateFlow(emptyList<Waypoint>())
        val waypointHolderFlow = MutableStateFlow(emptyList<Waypoint>())

        waypointRouteFlow.toWaypointRouteChangeList(waypointHolderFlow).test {
            waypointRouteFlow.emit(listOf(waypoint))
            waypointHolderFlow.emit(listOf(waypoint))

            val waypointChangeList = awaitItem()
            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly()
        }
    }

    @Test
    fun `GIVEN flow of waypoint route change list with a waypoint WHEN new waypoint replaces existing waypoint THEN new waypoint is active AND old waypoint is inactive`() = runTest {
        val waypoint = Waypoint()
        val waypointRouteFlow = MutableStateFlow(listOf(waypoint))
        val waypointHolderFlow = MutableStateFlow(listOf(waypoint))

        waypointRouteFlow.toWaypointRouteChangeList(waypointHolderFlow).test {
            var waypointChangeList = awaitItem()
            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly()

            val waypoint2 = Waypoint()
            waypointRouteFlow.emit(listOf(waypoint2))
            waypointHolderFlow.emit(listOf(waypoint, waypoint2))

            waypointChangeList = awaitItem()

            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint2)
            Truth.assertThat(waypointChangeList.inactive).containsExactly(waypoint)
            Truth.assertThat(waypointChangeList.removed).containsExactly()
        }
    }

    @Test
    fun `GIVEN flow of waypoint route change list with a waypoint WHEN new waypoint is added and old waypoint removed THEN new waypoint is active AND old waypoint is removed`() = runTest {
        val waypoint = Waypoint()
        val waypointRouteFlow = MutableStateFlow(listOf(waypoint))
        val waypointHolderFlow = MutableStateFlow(listOf(waypoint))

        waypointRouteFlow.toWaypointRouteChangeList(waypointHolderFlow).test {
            var waypointChangeList = awaitItem()
            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly()

            val waypoint2 = Waypoint()
            waypointRouteFlow.emit(listOf(waypoint2))
            waypointHolderFlow.emit(listOf(waypoint2))

            waypointChangeList = awaitItem()

            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint2)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly(waypoint)
        }
    }

    @Test
    fun `GIVEN flow of waypoint route change list with two waypoints WHEN one waypoint is added AND one waypoint is removed THEN waypoints are active, inactive and removed`() = runTest {
        val waypoint = Waypoint()
        val waypoint2 = Waypoint()
        val waypointRouteFlow = MutableStateFlow(listOf(waypoint, waypoint2))
        val waypointHolderFlow = MutableStateFlow(listOf(waypoint, waypoint2))

        waypointRouteFlow.toWaypointRouteChangeList(waypointHolderFlow).test {
            var waypointChangeList = awaitItem()
            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint, waypoint2)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly()

            val waypoint3 = Waypoint()
            waypointRouteFlow.emit(listOf(waypoint3))
            waypointHolderFlow.emit(listOf(waypoint2, waypoint3))

            waypointChangeList = awaitItem()

            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint3)
            Truth.assertThat(waypointChangeList.inactive).containsExactly(waypoint2)
            Truth.assertThat(waypointChangeList.removed).containsExactly(waypoint)
        }
    }

    @Test
    fun `GIVEN flow of waypoint route change list with two waypoints WHEN one waypoint is removed THEN waypoints are inactive and removed`() = runTest {
        val waypoint = Waypoint()
        val waypoint2 = Waypoint()
        val waypointRouteFlow = MutableStateFlow(listOf(waypoint, waypoint2))
        val waypointHolderFlow = MutableStateFlow(listOf(waypoint, waypoint2))

        waypointRouteFlow.toWaypointRouteChangeList(waypointHolderFlow).test {
            var waypointChangeList = awaitItem()
            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint, waypoint2)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly()

            waypointRouteFlow.emit(emptyList())
            waypointHolderFlow.emit(listOf(waypoint2))

            waypointChangeList = awaitItem()

            Truth.assertThat(waypointChangeList.active).containsExactly()
            Truth.assertThat(waypointChangeList.inactive).containsExactly(waypoint2)
            Truth.assertThat(waypointChangeList.removed).containsExactly(waypoint)
        }
    }

    @Test
    fun `GIVEN flow of waypoint route change list with a waypoint WHEN waypoint is removed THEN waypoint is removed`() = runTest {
        val waypoint = Waypoint()
        val waypointRouteFlow = MutableStateFlow(listOf(waypoint))
        val waypointHolderFlow = MutableStateFlow(listOf(waypoint))

        waypointRouteFlow.toWaypointRouteChangeList(waypointHolderFlow).test {
            var waypointChangeList = awaitItem()
            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly()

            waypointRouteFlow.emit(emptyList())
            waypointHolderFlow.emit(emptyList())

            waypointChangeList = awaitItem()

            Truth.assertThat(waypointChangeList.active).containsExactly()
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly(waypoint)
        }
    }

    @Test
    fun `GIVEN flow of waypoint route change list with a waypoint WHEN waypoint is remitted THEN nothing is emitted`() = runTest {
        val waypoint = Waypoint()
        val waypointRouteFlow = MutableStateFlow(listOf(waypoint))
        val waypointHolderFlow = MutableStateFlow(listOf(waypoint))

        waypointRouteFlow.toWaypointRouteChangeList(waypointHolderFlow).test {
            val waypointChangeList = awaitItem()
            Truth.assertThat(waypointChangeList.active).containsExactly(waypoint)
            Truth.assertThat(waypointChangeList.inactive).containsExactly()
            Truth.assertThat(waypointChangeList.removed).containsExactly()

            waypointRouteFlow.emit(listOf(waypoint))
            waypointHolderFlow.emit(listOf(waypoint))

            expectNoEvents()
        }
    }
}
