package com.squaredcandy.waypoint.core.action

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.scaffold.waypointScaffold
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointActionSenderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN no waypoint action sender THEN waypoint actions will fail`() = runTest {
        var actionSender: WaypointActionSender? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointScaffold {
                        actionSender = LocalWaypointActionSender.current
                    }
            )
        }

        Truth.assertThat(actionSender).isNotNull()
        Truth.assertThat(actionSender?.sendAction(NavigateWaypointAction(Waypoint()))?.isFailure)
            .isTrue()
    }

    @Test
    fun `GIVEN waypoint action sender THEN waypoint actions will succeed`() = runTest {
        var actionSender: WaypointActionSender? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(listOf())
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointScaffold {
                        actionSender = createWaypointActionSender()
                    }
            )
        }

        Truth.assertThat(actionSender).isNotNull()
        Truth.assertThat(actionSender?.invoke(NavigateWaypointAction(Waypoint()))?.isSuccess)
            .isTrue()
    }
}
