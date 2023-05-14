package com.squaredcandy.waypoint.core.action

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import kotlinx.coroutines.test.runTest
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

    @Test
    fun `GIVEN waypoints are added to parent and child waypoint lists WHEN waypoint is added to parent waypoint list THEN parent and child waypoint list is updated`() {
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
                            // Add it to the parent too
                            ModifierLocalMutableWaypointHolder.current
                                ?.parent
                                ?.updateWaypointList(WaypointNavigationType.Push) {
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
        waypointAction?.invoke(waypointHolder2!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder1?.waypointList).isEqualTo(list + newWaypoint)
        Truth.assertThat(waypointHolder2?.waypointList).isEqualTo(list + newWaypoint)
    }

    @Test
    fun `GIVEN that there is a waypoint holder, waypoint actions and waypoint holder WHEN waypoint actions is getting the current waypoint holder THEN the one above it is retrieved`() {
        val list = listOf(Waypoint())
        val list2 = listOf(Waypoint())

        var waypointHolder1: WaypointHolder? = null
        var waypointHolder2: WaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {
                        waypointHolder1 = ModifierLocalMutableWaypointHolder.current
                        addAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointHolder(list2)
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
        Truth.assertThat(waypointHolder2?.waypointList).isEqualTo(list2)
    }

    @Test
    fun `GIVEN we are trying to get the same waypoint holder in multiple places WHEN we compare waypoint holder lists THEN all lists should also be kept in sync`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder1: MutableWaypointHolder? = null
        var waypointHolder2: WaypointHolder? = null
        var waypointHolder3: WaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .modifierLocalConsumer {
                        waypointHolder1 = ModifierLocalMutableWaypointHolder.current
                        waypointActionProvider = ModifierLocalWaypointActionProvider.current
                    }
                    .waypointActions {
                        waypointHolder2 = ModifierLocalMutableWaypointHolder.current
                        addAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                            waypointHolder3 = ModifierLocalMutableWaypointHolder.current
                        }
                    }
                    .modifierLocalConsumer {
                        waypointActionProvider = ModifierLocalWaypointActionProvider.current
                    }
            )
        }

        Truth.assertThat(waypointHolder1).isNotNull()
        Truth.assertThat(waypointHolder2).isNotNull()
        Truth.assertThat(waypointHolder3).isNull()
        Truth.assertThat(waypointActionProvider).isNotNull()
        Truth.assertThat(waypointHolder1?.waypointList).isEqualTo(list)
        Truth.assertThat(waypointHolder2?.waypointList).isEqualTo(list)

        val waypointAction = waypointActionProvider?.getAction<NavigateWaypointAction>()
        waypointAction?.invoke(waypointHolder1!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder3).isNotNull()
        Truth.assertThat(waypointHolder1?.waypointList).isEqualTo(list + newWaypoint)
        Truth.assertThat(waypointHolder2?.waypointList).isEqualTo(list + newWaypoint)
        Truth.assertThat(waypointHolder3?.waypointList).isEqualTo(list + newWaypoint)
    }

    @Test
    fun `GIVEN parent waypoint actions are merged WHEN waypoint is added via actions THEN waypoint list is updated`() = runTest {
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
                    .waypointActions {
                        addAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                removeIf { it.id == waypointAction.waypointId }
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
        val navigateWaypointResolver = waypointActionProvider?.getAction<NavigateWaypointAction>()
        navigateWaypointResolver?.invoke(waypointHolder!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list + newWaypoint)
        val backtrackWaypointResolver = waypointActionProvider?.getAction<BacktrackWaypointAction>()
        backtrackWaypointResolver?.invoke(waypointHolder!!, BacktrackWaypointAction(newWaypoint.id))
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
    }

    @Test
    fun `GIVEN parent waypoint actions are not merged WHEN waypoint is added via actions THEN waypoint list is not updated`() = runTest {
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
                    .waypointActions(mergeParentActions = false) {
                        addAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                removeIf { it.id == waypointAction.waypointId }
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
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
        val backtrackWaypointResolver = waypointActionProvider?.getAction<BacktrackWaypointAction>()
        backtrackWaypointResolver?.invoke(waypointHolder!!, BacktrackWaypointAction(list.first().id))
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(emptyList<Waypoint>())
    }
}
