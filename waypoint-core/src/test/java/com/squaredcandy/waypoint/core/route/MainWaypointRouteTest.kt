package com.squaredcandy.waypoint.core.route

import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.holder.DefaultWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import kotlinx.coroutines.test.runTest
import org.junit.Test

class MainWaypointRouteTest {
    @Test
    fun `GIVEN empty waypoint holder THEN waypoint list is empty and cannot backtrack`() = runTest {
        val waypointHolder = DefaultWaypointHolder(mutableListOf())
        val waypointRoute = MainWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.waypointList).isEmpty()
        Truth.assertThat(waypointRoute.canBacktrack).isFalse()
    }

    @Test
    fun `GIVEN a non empty waypoint holder THEN waypoint list is not empty and cannot backtrack`() = runTest {
        val waypoint = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint))
        val waypointRoute = MainWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.waypointList).isNotEmpty()
        Truth.assertThat(waypointRoute.canBacktrack).isFalse()
    }

    @Test
    fun `GIVEN a waypoint holder with multiple waypoints THEN waypoint list is not empty and can backtrack`() = runTest {
        val waypoint1 = Waypoint()
        val waypoint2 = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint1, waypoint2))
        val waypointRoute = MainWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.waypointList).isNotEmpty()
        Truth.assertThat(waypointRoute.canBacktrack).isTrue()
    }

    @Test
    fun `GIVEN waypoint nav type is not pop WHEN getting transition spec type with existing waypoint THEN it is backtrack enter`() = runTest {
        val waypoint1 = Waypoint()
        val waypoint2 = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint1, waypoint2))
        val waypointRoute = MainWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.getWaypointTransitionSpecType(waypoint1)).isEqualTo(WaypointTransitionSpecType.NavigateEnter)
    }

    @Test
    fun `GIVEN waypoint nav type is not pop WHEN getting transition spec type with new waypoint THEN it is navigate exit`() = runTest {
        val waypoint1 = Waypoint()
        val waypoint2 = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint1))
        val waypointRoute = MainWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.getWaypointTransitionSpecType(waypoint2)).isEqualTo(WaypointTransitionSpecType.NavigateExit)
    }

    @Test
    fun `GIVEN waypoint nav type is pop WHEN getting transition spec type with existing waypoint THEN it is backtrack enter`() = runTest {
        val waypoint1 = Waypoint()
        val waypoint2 = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint1, waypoint2))
        waypointHolder.updateWaypointList(WaypointNavigationType.Pop) { removeLast() }
        val waypointRoute = MainWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.getWaypointTransitionSpecType(waypoint1)).isEqualTo(WaypointTransitionSpecType.BacktrackEnter)
    }

    @Test
    fun `GIVEN waypoint nav type is pop WHEN getting transition spec type with new waypoint THEN it is navigate exit`() = runTest {
        val waypoint1 = Waypoint()
        val waypoint2 = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf(waypoint1, waypoint2))
        waypointHolder.updateWaypointList(WaypointNavigationType.Pop) { removeLast() }
        val waypointRoute = MainWaypointRoute(waypointHolder)
        Truth.assertThat(waypointRoute.getWaypointTransitionSpecType(waypoint2)).isEqualTo(WaypointTransitionSpecType.BacktrackExit)
    }
}
