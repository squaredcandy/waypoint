package com.squaredcandy.waypoint.core.action

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalComposeUiApi::class)
class WaypointActionsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN waypoint is added via actions THEN waypoint list is updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {
                        addAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalMutableWaypointHolder.current
                        waypointActionProvider = ModifierLocalWaypointActionProvider.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        Truth.assertThat(waypointActionProvider).isNotNull()
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
        val waypointAction = waypointActionProvider?.getAction<NavigateWaypointAction>()
        waypointAction?.invoke(waypointHolder!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list + newWaypoint)
    }

    @Test
    fun `GIVEN no actions exist WHEN waypoint is added via actions THEN waypoint list is not updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {}
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalMutableWaypointHolder.current
                        waypointActionProvider = ModifierLocalWaypointActionProvider.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        Truth.assertThat(waypointActionProvider).isNotNull()
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
        val waypointAction = waypointActionProvider?.getAction<NavigateWaypointAction>()
        waypointAction?.invoke(waypointHolder!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
    }

    @Test
    fun `WHEN waypoint is added to parent waypoint list THEN parent waypoint list is updated AND child waypoint list is not changed`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder1: WaypointHolder? = null
        var waypointHolder2: MutableWaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .modifierLocalConsumer {
                        waypointHolder1 = ModifierLocalWaypointHolder.current
                    }
                    .waypointHolder(list)
                    .waypointActions {
                        addAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .modifierLocalConsumer {
                        waypointHolder2 = ModifierLocalMutableWaypointHolder.current
                        waypointActionProvider = ModifierLocalWaypointActionProvider.current
                    }
            )
        }

        Truth.assertThat(waypointHolder1).isNotNull()
        Truth.assertThat(waypointHolder2).isNotNull()
        Truth.assertThat(waypointActionProvider).isNotNull()
        Truth.assertThat(waypointHolder1?.waypointList).isEqualTo(list)
        Truth.assertThat(waypointHolder2?.waypointList).isEqualTo(list)
        Truth.assertThat(waypointHolder2?.parent).isEqualTo(waypointHolder1)
        val waypointAction = waypointActionProvider?.getAction<NavigateWaypointAction>()
        waypointAction?.invoke(waypointHolder2!!.parent!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder1?.waypointList).isEqualTo(list + newWaypoint)
        Truth.assertThat(waypointHolder2?.waypointList).isEqualTo(list)
    }

    @Test
    fun `GIVEN waypoint action has hooks WHEN waypoint is added via actions THEN waypoint list is updated AND hooks are invoked`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        var preHookReceived = false
        var postHookReceived = false
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {
                        addAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                        addHook<NavigateWaypointAction>(
                            preResolveHook = { _, _ ->
                                preHookReceived = true
                            },
                            postResolveHook = { _, _ ->
                                postHookReceived = true
                            },
                        )
                    }
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalMutableWaypointHolder.current
                        waypointActionProvider = ModifierLocalWaypointActionProvider.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        Truth.assertThat(waypointActionProvider).isNotNull()
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
        val waypointAction = waypointActionProvider?.getAction<NavigateWaypointAction>()
        waypointAction?.invoke(waypointHolder!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list + newWaypoint)
        Truth.assertThat(preHookReceived).isTrue()
        Truth.assertThat(postHookReceived).isTrue()
    }
}
