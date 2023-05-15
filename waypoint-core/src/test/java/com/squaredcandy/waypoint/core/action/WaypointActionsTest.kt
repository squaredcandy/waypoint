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
import org.junit.Ignore
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
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
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

    @Ignore("Figure out a way to update a modifier parameter inputs")
    @Test
    fun `WHEN waypoint actions are changed THEN action list is updated`() {}

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
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
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
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
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
    fun `GIVEN waypoint action has multiple hooks WHEN waypoint is added via actions THEN waypoint list is updated AND all hooks are invoked`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        var preHookReceived = 0
        var postHookReceived = 0
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                        addHook<NavigateWaypointAction>(
                            preResolveHook = { _, _ ->
                                preHookReceived++
                            },
                            postResolveHook = { _, _ ->
                                postHookReceived++
                            },
                        )
                        addHook<NavigateWaypointAction>(
                            preResolveHook = { _, _ ->
                                preHookReceived++
                            },
                            postResolveHook = { _, _ ->
                                postHookReceived++
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
        Truth.assertThat(preHookReceived).isEqualTo(2)
        Truth.assertThat(postHookReceived).isEqualTo(2)
    }

    @Test
    fun `GIVEN waypoint action has hooks across multiple actions WHEN waypoint is added via actions THEN waypoint list is updated AND hooks are invoked oldest to newest`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        val baseNumber = 10
        var preHookReceived = 0
        var postHookReceived = 0
        var preHookReceived2 = 0
        var postHookReceived2 = 0
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {
                        addHook<NavigateWaypointAction>(
                            preResolveHook = { _, _ ->
                                preHookReceived = baseNumber
                            },
                            postResolveHook = { _, _ ->
                                postHookReceived = baseNumber
                            },
                        )
                    }
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                        addHook<NavigateWaypointAction>(
                            preResolveHook = { _, _ ->
                                preHookReceived2 = preHookReceived
                            },
                            postResolveHook = { _, _ ->
                                postHookReceived2 = postHookReceived
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
        Truth.assertThat(preHookReceived).isEqualTo(baseNumber)
        Truth.assertThat(postHookReceived).isEqualTo(baseNumber)
        Truth.assertThat(preHookReceived2).isEqualTo(baseNumber)
        Truth.assertThat(postHookReceived2).isEqualTo(baseNumber)
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
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                            // Add it to the parent too
                            waypointHolder.parent
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
                    .modifierLocalConsumer {
                        waypointHolder1 = ModifierLocalMutableWaypointHolder.current
                    }
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
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
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointActions {
                        onAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
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
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointActions(mergeParentActions = false) {
                        onAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
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
