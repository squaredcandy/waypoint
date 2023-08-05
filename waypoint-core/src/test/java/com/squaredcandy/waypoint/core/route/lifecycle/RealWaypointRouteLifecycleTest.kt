package com.squaredcandy.waypoint.core.route.lifecycle

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.squaredcandy.waypoint.core.route.LocalWaypoint
import com.squaredcandy.waypoint.core.route.MainWaypointRoute
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes
import com.squaredcandy.waypoint.core.scaffold.waypointScaffold
import com.squaredcandy.waypoint.core.semantics.invokeWaypointAction
import com.squaredcandy.waypoint.core.semantics.onWaypointActionProviderNode
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointRouteLifecycleTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN waypoint route and holder THEN waypoint route lifecycle is valid`() = runTest {
        val list = listOf(Waypoint())
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointRoutes {
                        addRoute(::MainWaypointRoute)
                    }
                    .waypointScaffold {
                        val waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                        val mainWaypointRoute =
                            waypointRouteProvider?.getRoute(MainWaypointRoute.key)!!

                        val waypointList by remember {
                            derivedStateOf { mainWaypointRoute.waypointList }
                        }
                        val waypoint by remember {
                            derivedStateOf { waypointList.last() }
                        }

                        val routeLifecycle = rememberWaypointRouteLifecycle(mainWaypointRoute)
                        routeLifecycle.WithLifecycle(waypoint = waypoint) {
                            val currentWaypoint = LocalWaypoint.current
                            Truth.assertThat(currentWaypoint).isEqualTo(waypoint)
                        }
                    }
            )
        }
    }

    @Test
    fun `GIVEN waypoint route and holder WHEN waypoint is changed THEN waypoint route lifecycle is also changed`() = runTest {
        val waypoint1 = Waypoint()
        val waypoint2 = Waypoint()
        val list = listOf(waypoint1)
        var recompositionCount = 0
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
                    .waypointRoutes {
                        addRoute(::MainWaypointRoute)
                    }
                    .waypointScaffold {
                        val waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                        val mainWaypointRoute =
                            waypointRouteProvider?.getRoute(MainWaypointRoute.key)!!

                        val waypointList by remember {
                            derivedStateOf { mainWaypointRoute.waypointList }
                        }
                        val waypoint by remember {
                            derivedStateOf { waypointList.last() }
                        }

                        val routeLifecycle = rememberWaypointRouteLifecycle(mainWaypointRoute)
                        routeLifecycle.WithLifecycle(waypoint = waypoint) {
                            val currentWaypoint = LocalWaypoint.current
                            val assertingWaypoint = if (recompositionCount == 0) {
                                waypoint1
                            } else {
                                waypoint2
                            }
                            Truth.assertThat(currentWaypoint).isEqualTo(assertingWaypoint)
                            recompositionCount++
                        }
                    }
            )
        }

        composeTestRule.onWaypointActionProviderNode()
            .invokeWaypointAction(NavigateWaypointAction(waypoint2))

        composeTestRule.waitForIdle()
        Truth.assertThat(recompositionCount).isEqualTo(2)
    }
}
