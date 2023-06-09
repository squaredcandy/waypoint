package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.content.waypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.route.MainWaypointRoute
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes
import com.squaredcandy.waypoint.core.semantics.assertWaypointListEqualTo
import com.squaredcandy.waypoint.core.semantics.invokeWaypointAction
import com.squaredcandy.waypoint.core.semantics.onWaypointActionProviderNode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointLifecycleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN waypoint holder and lifecycle WHEN getting waypoint lifecycle owner THEN it is not null`() {
        var waypointLifecycleOwner: WaypointLifecycleOwner? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointLifecycle()
                    .modifierLocalConsumer {
                        waypointLifecycleOwner = ModifierLocalWaypointLifecycleOwner.current
                    },
            )
        }

        Truth.assertThat(waypointLifecycleOwner).isNotNull()
    }

    @Test
    fun `GIVEN waypoint lifecycle WHEN backtrack waypoint action is called THEN saved state holder will call remove state`() {
        val waypoint = Waypoint()
        val waypointList = listOf(waypoint)
        var onRemoveCalled = false
        val savedStateHolder = TestSavedStateHolder {
            onRemoveCalled = true
        }
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(waypointList)
                    .waypointActions {
                        onAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Pop) {
                                removeIf { it.id == waypointAction.waypointId }
                            }
                        }
                    }
                    .waypointLifecycle(savedStateHolder)
            )
        }

        composeTestRule.onWaypointActionProviderNode()
            .invokeWaypointAction<BacktrackWaypointAction>(BacktrackWaypointAction(waypoint.id))
            .assertWaypointListEqualTo(emptyList())

        Truth.assertThat(onRemoveCalled).isTrue()
    }

    @Test
    fun `GIVEN waypoint holder, route, lifecycle WHEN setting up waypoint content THEN actions should fail`() {
        val waypointList = listOf(Waypoint())
        var waypointContext: WaypointContext? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(waypointList)
                    .waypointRoutes {
                        addRoute(::MainWaypointRoute)
                    }
                    .waypointLifecycle()
                    .waypointContent {
                        val lifecycleOwner = ModifierLocalWaypointLifecycleOwner.currentOrNull
                        val routeProvider = ModifierLocalWaypointRouteProvider.currentOrNull
                        val mainRoute = routeProvider?.getRoute(MainWaypointRoute.key) ?: return@waypointContent
                        val currentWaypoint = mainRoute.waypointList.last()
                        lifecycleOwner?.WithLifecycle(currentWaypoint) {
                            waypointContext = this
                        }
                    }
            )
        }

        val result = waypointContext?.sendAction(NavigateWaypointAction::class, NavigateWaypointAction(Waypoint()))
        Truth.assertThat(result?.isFailure).isTrue()
    }

    @Test
    fun `GIVEN waypoint holder, lifecycle, actions, routes WHEN setting up waypoint content THEN actions should fail`() {
        val waypointList = listOf(Waypoint())
        var waypointContext: WaypointContext? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(waypointList)
                    .waypointLifecycle()
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
                    .waypointContent {
                        val lifecycleOwner = ModifierLocalWaypointLifecycleOwner.currentOrNull
                        val routeProvider = ModifierLocalWaypointRouteProvider.currentOrNull
                        val mainRoute = routeProvider?.getRoute(MainWaypointRoute.key) ?: return@waypointContent
                        val currentWaypoint = mainRoute.waypointList.last()
                        lifecycleOwner?.WithLifecycle(currentWaypoint) {
                            waypointContext = this
                        }
                    }
            )
        }

        val result = waypointContext?.sendAction(NavigateWaypointAction::class, NavigateWaypointAction(Waypoint()))
        Truth.assertThat(result?.isFailure).isTrue()
    }

    @Test
    fun `GIVEN waypoint holder, actions, lifecycle, routes WHEN setting up waypoint content THEN actions should succeed`() {
        val waypointList = listOf(Waypoint())
        var waypointContext: WaypointContext? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(waypointList)
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointLifecycle()
                    .waypointRoutes {
                        addRoute(::MainWaypointRoute)
                    }
                    .waypointContent {
                        val lifecycleOwner = ModifierLocalWaypointLifecycleOwner.currentOrNull
                        val routeProvider = ModifierLocalWaypointRouteProvider.currentOrNull
                        val mainRoute = routeProvider?.getRoute(MainWaypointRoute.key) ?: return@waypointContent
                        val currentWaypoint = mainRoute.waypointList.last()
                        lifecycleOwner?.WithLifecycle(currentWaypoint) {
                            waypointContext = this
                        }
                    }
            )
        }

        val result = waypointContext?.sendAction(NavigateWaypointAction::class, NavigateWaypointAction(Waypoint()))
        Truth.assertThat(result?.isSuccess).isTrue()
    }

    @Test
    fun `GIVEN waypoint holder, actions, routes, lifecycle WHEN setting up waypoint content THEN actions should succeed`() {
        val waypointList = listOf(Waypoint())
        var waypointContext: WaypointContext? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(waypointList)
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
                    .waypointLifecycle()
                    .waypointContent {
                        val lifecycleOwner = ModifierLocalWaypointLifecycleOwner.currentOrNull
                        val routeProvider = ModifierLocalWaypointRouteProvider.currentOrNull
                        val mainRoute = routeProvider?.getRoute(MainWaypointRoute.key) ?: return@waypointContent
                        val currentWaypoint = mainRoute.waypointList.last()
                        lifecycleOwner?.WithLifecycle(currentWaypoint) {
                            waypointContext = this
                        }
                    }
            )
        }

        val result = waypointContext?.sendAction(NavigateWaypointAction::class, NavigateWaypointAction(Waypoint()))
        Truth.assertThat(result?.isSuccess).isTrue()
    }
}
