package com.squaredcandy.waypoint.core.holder

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.semantics.assertWaypointListEqualTo
import com.squaredcandy.waypoint.core.semantics.assertWaypointListInstanceEqualAfter
import com.squaredcandy.waypoint.core.semantics.assertWaypointListInstanceNotEqualAfter
import com.squaredcandy.waypoint.core.semantics.onWaypointHolderNode
import com.squaredcandy.waypoint.core.semantics.updateWaypointList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointHolderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN waypoint is added THEN waypoint list is updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
            )
        }
        composeTestRule.onWaypointHolderNode()
            .assertWaypointListEqualTo(list)
            .updateWaypointList(WaypointNavigationType.Push) { add(newWaypoint) }
            .assertWaypointListEqualTo(list + newWaypoint)
    }

    @Test
    fun `WHEN waypoint is removed from list THEN waypoint list is updated`() {
        val list = listOf(Waypoint(), Waypoint())

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
            )
        }

        composeTestRule.onWaypointHolderNode()
            .assertWaypointListEqualTo(list)
            .updateWaypointList(WaypointNavigationType.Push) { if(lastIndex >= 0) removeAt(lastIndex) }
            .assertWaypointListEqualTo(list.dropLast(1))
    }

    @Test
    fun `WHEN waypoint list is updated THEN new immutable waypoint list is returned`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
            )
        }

        composeTestRule.onWaypointHolderNode()
            .assertWaypointListInstanceNotEqualAfter {
                this.updateWaypointList(WaypointNavigationType.Push) { add(newWaypoint) }
            }
    }

    @Test
    fun `GIVEN nothing has changed WHEN getting waypoint list twice THEN the same instance is returned`() {
        val list = listOf(Waypoint())

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
            )
        }

        composeTestRule.onWaypointHolderNode()
            .assertWaypointListInstanceEqualAfter { this }
    }

    @Test
    fun `GIVEN waypoint list is updated WHEN configuration changes THEN waypoint list is preserved`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
            )
        }

        composeTestRule.onWaypointHolderNode()
            .updateWaypointList(WaypointNavigationType.Push) { add(newWaypoint) }
            .assertWaypointListEqualTo(list + newWaypoint)
        stateRestorationTester.emulateSavedInstanceStateRestore()
        composeTestRule.onWaypointHolderNode()
            .assertWaypointListEqualTo(list + newWaypoint)
    }
}
