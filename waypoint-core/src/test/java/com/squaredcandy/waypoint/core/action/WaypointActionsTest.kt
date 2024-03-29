package com.squaredcandy.waypoint.core.action

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.mutableStateOf
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
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.semantics.assertAllWaypointListEqualTo
import com.squaredcandy.waypoint.core.semantics.assertCorrectWaypointHolderParentTree
import com.squaredcandy.waypoint.core.semantics.assertWaypointActionDoesNotExist
import com.squaredcandy.waypoint.core.semantics.assertWaypointActionExists
import com.squaredcandy.waypoint.core.semantics.assertWaypointListEqualTo
import com.squaredcandy.waypoint.core.semantics.invokeWaypointAction
import com.squaredcandy.waypoint.core.semantics.invokeWaypointActionOnNode
import com.squaredcandy.waypoint.core.semantics.onWaypointActionProviderNode
import com.squaredcandy.waypoint.core.semantics.onWaypointActionProviderNodes
import com.squaredcandy.waypoint.core.semantics.onWaypointHolderNode
import com.squaredcandy.waypoint.core.semantics.onWaypointHolderNodes
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
            )
        }

        composeTestRule.onWaypointActionProviderNode()
            .assertWaypointListEqualTo(list)
            .assertWaypointActionExists<NavigateWaypointAction>()
            .invokeWaypointAction<NavigateWaypointAction>(NavigateWaypointAction(newWaypoint))
            .assertWaypointListEqualTo(list + newWaypoint)
    }

    @Test
    fun `GIVEN no actions exist WHEN waypoint is added via actions THEN waypoint list is not updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {}
            )
        }

        composeTestRule.onWaypointActionProviderNode()
            .assertWaypointActionDoesNotExist<NavigateWaypointAction>()
            .invokeWaypointAction<NavigateWaypointAction>(NavigateWaypointAction(newWaypoint))
            .assertWaypointListEqualTo(list)
    }

    @Test
    fun `WHEN waypoint actions are changed THEN action list is updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        val waypointActionBuilderState = mutableStateOf<WaypointActionSetBuilder.() -> Unit>({})
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions(builder = waypointActionBuilderState.value)
            )
        }

        composeTestRule.onWaypointActionProviderNode()
            .assertWaypointActionDoesNotExist<NavigateWaypointAction>()
            .invokeWaypointAction<NavigateWaypointAction>(NavigateWaypointAction(newWaypoint))
            .assertWaypointListEqualTo(list)

        waypointActionBuilderState.value = {
            onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                    add(waypointAction.waypoint)
                }
            }
        }
        composeTestRule.waitForIdle()

        composeTestRule.onWaypointActionProviderNode()
            .assertWaypointActionExists<NavigateWaypointAction>()
            .invokeWaypointAction<NavigateWaypointAction>(NavigateWaypointAction(newWaypoint))
            .assertWaypointListEqualTo(list + newWaypoint)
    }

    @Test
    fun `WHEN waypoint is added to parent waypoint list THEN parent waypoint list is updated AND child waypoint list is not changed`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
            ) {
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
                )
            }
        }

        composeTestRule.onWaypointHolderNodes()
            .assertAllWaypointListEqualTo(list)
            .assertCorrectWaypointHolderParentTree()

        composeTestRule.onWaypointActionProviderNode()
            .assertWaypointActionExists<NavigateWaypointAction>()
            .invokeWaypointActionOnNode<NavigateWaypointAction>(
                node = { composeTestRule.onWaypointHolderNodes()[0] },
                action = NavigateWaypointAction(newWaypoint),
            )

        composeTestRule.onWaypointHolderNodes()
            .assertWaypointListEqualTo(0, list + newWaypoint)
            .assertWaypointListEqualTo(1, list)
    }

    @Test
    fun `GIVEN parent and child waypoint lists receive waypoints WHEN waypoint is added THEN both waypoint lists are updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
            ) {
                Box(
                    modifier = Modifier
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
                )
            }
        }

        composeTestRule.onWaypointHolderNodes()
            .assertAllWaypointListEqualTo(list)
            .assertCorrectWaypointHolderParentTree()

        composeTestRule.onWaypointActionProviderNode()
            .assertWaypointActionExists<NavigateWaypointAction>()
            .invokeWaypointAction<NavigateWaypointAction>(NavigateWaypointAction(newWaypoint))

        composeTestRule.onWaypointHolderNodes()
            .assertAllWaypointListEqualTo(list + newWaypoint)
    }

    @Test
    fun `GIVEN waypoint holder, actions, holder WHEN getting waypoint holder THEN the correct one is retrieved`() {
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
            ) {
                Box(
                    modifier = Modifier
                        .waypointActions {
                            onAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
                                waypointHolder.updateWaypointList(WaypointNavigationType.Pop) {
                                    removeIf { it.id == waypointAction.waypointId }
                                }
                            }
                        }
                )
            }
        }

        composeTestRule.onWaypointActionProviderNodes()[0]
            .assertWaypointListEqualTo(list)
            .assertWaypointActionExists<NavigateWaypointAction>()
            .invokeWaypointAction<NavigateWaypointAction>(NavigateWaypointAction(newWaypoint))
            .assertWaypointListEqualTo(list + newWaypoint)

        composeTestRule.onWaypointActionProviderNodes()[1]
            .assertWaypointActionExists<NavigateWaypointAction>()
            .assertWaypointActionExists<BacktrackWaypointAction>()
            .invokeWaypointActionOnNode<BacktrackWaypointAction>(
                node = { composeTestRule.onWaypointHolderNode() },
                action = BacktrackWaypointAction(newWaypoint.id),
            )

        composeTestRule.onWaypointHolderNode()
            .assertWaypointListEqualTo(list)
    }

    @Test
    fun `GIVEN parent waypoint actions are not merged WHEN waypoint is added via actions THEN waypoint list is not updated`() = runTest {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

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
            ) {
                Box(
                    modifier = Modifier
                        .waypointActions(mergeParentActions = false) {
                            onAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
                                waypointHolder.updateWaypointList(WaypointNavigationType.Pop) {
                                    removeIf { it.id == waypointAction.waypointId }
                                }
                            }
                        }
                )
            }
        }

        composeTestRule.onWaypointActionProviderNodes()[1]
            .assertWaypointActionDoesNotExist<NavigateWaypointAction>()
            .invokeWaypointActionOnNode<NavigateWaypointAction>(
                node = { composeTestRule.onWaypointHolderNode() },
                action = NavigateWaypointAction(newWaypoint),
            )

        composeTestRule.onWaypointHolderNode()
            .assertWaypointListEqualTo(list)

        composeTestRule.onWaypointActionProviderNodes()[1]
            .assertWaypointActionExists<BacktrackWaypointAction>()
            .invokeWaypointActionOnNode<BacktrackWaypointAction>(
                node = { composeTestRule.onWaypointHolderNode() },
                action = BacktrackWaypointAction(list.first().id),
            )

        composeTestRule.onWaypointHolderNode()
            .assertWaypointListEqualTo(emptyList())
    }
}
