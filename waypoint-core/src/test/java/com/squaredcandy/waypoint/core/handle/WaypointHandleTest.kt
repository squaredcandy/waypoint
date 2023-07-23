package com.squaredcandy.waypoint.core.handle

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.scaffold.waypointScaffold
import com.squaredcandy.waypoint.core.semantics.assertWaypointHandleDoesNotExist
import com.squaredcandy.waypoint.core.semantics.assertWaypointHandleExists
import com.squaredcandy.waypoint.core.semantics.assertWaypointListEqualTo
import com.squaredcandy.waypoint.core.semantics.invokeWithWaypointHandle
import com.squaredcandy.waypoint.core.semantics.onWaypointHandleNode
import com.squaredcandy.waypoint.core.semantics.onWaypointHolderNode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointHandleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN waypoint holder and waypoint actions is missing THEN waypoint handle provider is not available`() {
        val waypoint = Waypoint()
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHandleProvider()
            )
        }

        composeTestRule.onWaypointHandleNode()
            .assertWaypointHandleDoesNotExist(waypoint, ::DefaultWaypointHandle)
    }

    @Test
    fun `WHEN waypoint holder and waypoint actions is present THEN waypoint handle provider is available`() {
        val waypoint = Waypoint()
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(listOf(waypoint))
                    .waypointActions {}
                    .waypointHandleProvider()
            )
        }

        composeTestRule.onWaypointHandleNode()
            .assertWaypointHandleExists(waypoint, ::DefaultWaypointHandle)
    }

    @Test
    fun `GIVEN waypoint handle is available AND action is available WHEN waypoint handle sends actions THEN action is processed by waypoint actions`() {
        val waypoint = Waypoint()
        val newWaypoint = Waypoint()
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(listOf(waypoint))
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointHandleProvider()
            )
        }

        composeTestRule.onWaypointHandleNode()
            .assertWaypointHandleExists(waypoint, ::DefaultWaypointHandle)
            .invokeWithWaypointHandle(waypoint, ::DefaultWaypointHandle) { handle ->
                handle.sendAction(NavigateWaypointAction(newWaypoint))
            }
            .assertWaypointListEqualTo(listOf(waypoint, newWaypoint))
    }

    @Test
    fun `GIVEN waypoint handle is available AND action is not available WHEN waypoint handle sends actions THEN action is not processed`() {
        val waypoint = Waypoint()
        val newWaypoint = Waypoint()
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(listOf(waypoint))
                    .waypointActions {}
                    .waypointHandleProvider()
            )
        }

        composeTestRule.onWaypointHandleNode()
            .assertWaypointHandleExists(waypoint, ::DefaultWaypointHandle)
            .invokeWithWaypointHandle(waypoint, ::DefaultWaypointHandle) { handle ->
                handle.sendAction(NavigateWaypointAction(newWaypoint))
            }
            .assertWaypointListEqualTo(listOf(waypoint))
    }

    @Test
    fun `WHEN waypoint handle's composition locals are not available THEN remember waypoint handle is not available`() {
        val waypoint = Waypoint()
        val result = runCatching {
            composeTestRule.setContent {
                Box(
                    modifier = Modifier
                        .waypointHolder(listOf(waypoint))
                        .waypointActions {}
                        .waypointHandleProvider()
                        .waypointScaffold {
                            rememberWaypointHandle(::DefaultWaypointHandle)
                        }
                )
            }
        }

        Truth.assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `WHEN waypoint handle's composition locals are available THEN remember waypoint handle is available`() {
        val waypoint = Waypoint()
        val newWaypoint = Waypoint()
        var handle: DefaultWaypointHandle? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(listOf(waypoint))
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointHandleProvider()
                    .waypointScaffold {
                        CompositionLocalProvider(
                            LocalWaypointHandleProvider provides ModifierLocalWaypointHandleProvider.current,
                            LocalWaypoint provides waypoint,
                        ) {
                            handle = rememberWaypointHandle(::DefaultWaypointHandle)
                        }
                    }
            )
        }

        Truth.assertThat(handle).isNotNull()
        handle?.sendAction(NavigateWaypointAction(newWaypoint))

        composeTestRule.onWaypointHolderNode()
            .assertWaypointListEqualTo(listOf(waypoint, newWaypoint))
    }
}
