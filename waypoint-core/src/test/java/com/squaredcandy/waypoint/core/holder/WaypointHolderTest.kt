package com.squaredcandy.waypoint.core.holder

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalComposeUiApi::class)
class WaypointHolderTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN waypoint is added THEN waypoint list is updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalMutableWaypointHolder.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
        waypointHolder?.updateWaypointList(WaypointNavigationType.Push) { add(newWaypoint) }
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list + newWaypoint)
    }

    @Test
    fun `WHEN waypoint is removed from list THEN waypoint list is updated`() {
        val list = listOf(Waypoint(), Waypoint())

        var waypointHolder: MutableWaypointHolder? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalMutableWaypointHolder.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
        waypointHolder?.updateWaypointList(WaypointNavigationType.Push) { if(lastIndex >= 0) removeAt(lastIndex) }
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list.dropLast(1))
    }

    @Test
    fun `WHEN after waypoint list is updated THEN new immutable waypoint list is returned`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalMutableWaypointHolder.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        val initialWaypointList = waypointHolder?.waypointList
        waypointHolder?.updateWaypointList(WaypointNavigationType.Push) { add(newWaypoint) }
        val newWaypointList = waypointHolder?.waypointList
        Truth.assertThat(initialWaypointList).isNotEqualTo(newWaypointList)
        Truth.assertThat(initialWaypointList).isNotSameInstanceAs(newWaypointList)
    }

    @Test
    fun `GIVEN nothing has changed WHEN getting waypoint list twice THEN the same instance is returned`() {
        val list = listOf(Waypoint())

        var waypointHolder: WaypointHolder? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalWaypointHolder.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        Truth.assertThat(waypointHolder?.waypointList).isSameInstanceAs(waypointHolder?.waypointList)
    }
}
