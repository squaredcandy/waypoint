package com.squaredcandy.waypoint.simple

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.LocalWaypointActionSender
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.createWaypointActionSender
import com.squaredcandy.waypoint.core.action.invoke
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.feature.transition.DefaultScreenTransition
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.route.MainWaypointRoute
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.SideWaypointRoute
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.core.route.lifecycle.rememberWaypointRouteLifecycle
import com.squaredcandy.waypoint.core.route.waypointRoutes
import com.squaredcandy.waypoint.core.scaffold.WaypointScaffoldScope
import com.squaredcandy.waypoint.core.scaffold.waypointScaffold
import com.squaredcandy.waypoint.util.getTransition

@Composable
private fun WaypointScaffoldScope.Navigation(
    waypointRouteProvider: WaypointRouteProvider,
    fallbackTransition: WaypointTransition = DefaultScreenTransition,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MainWaypointRoute(
            waypointRouteProvider = waypointRouteProvider,
            fallbackTransition = fallbackTransition,
        )

        SideWaypointRoute(
            waypointRouteProvider = waypointRouteProvider,
        )
    }
}

@Composable
private fun WaypointScaffoldScope.MainWaypointRoute(
    waypointRouteProvider: WaypointRouteProvider,
    fallbackTransition: WaypointTransition,
) {
    val mainWaypointRoute by remember {
        derivedStateOf { waypointRouteProvider.getRoute(MainWaypointRoute.key) }
    }
    val mainWaypointList by remember {
        derivedStateOf { mainWaypointRoute.waypointList }
    }

    val routeLifecycle = rememberWaypointRouteLifecycle(mainWaypointRoute)
    val actionSender = createWaypointActionSender()

    if (mainWaypointList.isNotEmpty()) {
        // To be used later own to decide what the user is focused on
        var currentlyFocused by remember { mutableStateOf(MainWaypointRoute.key) }
        val mainWaypoint by remember {
            derivedStateOf { mainWaypointList.last() }
        }
        AnimatedContent(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(currentlyFocused) {
                    awaitPointerEventScope {
                        if (currentEvent.type == PointerEventType.Press) {
                            currentlyFocused = MainWaypointRoute.key
                        }
                    }
                },
            targetState = mainWaypoint,
            label = "main_waypoint",
            transitionSpec = {
                getTransition(
                    waypointTransitionSpecType = mainWaypointRoute.getWaypointTransitionSpecType(initialState),
                    fallbackTransition = fallbackTransition,
                )
            },
            contentKey = (Waypoint::id),
        ) { waypoint ->
            routeLifecycle.WithLifecycle(
                waypoint,
                LocalWaypointActionSender provides actionSender,
            ) {
                BackHandler(enabled = mainWaypointRoute.canBacktrack) {
                    actionSender(BacktrackWaypointAction(waypoint.id))
                }
                waypoint.feature
                    .getContent()
                    .Content()
            }
        }
    }
}

@Composable
private fun WaypointScaffoldScope.SideWaypointRoute(
    waypointRouteProvider: WaypointRouteProvider,
) {
    val sideWaypointRoute by remember {
        derivedStateOf { waypointRouteProvider.getRoute(SideWaypointRoute.key) }
    }
    val sideWaypointList by remember {
        derivedStateOf { sideWaypointRoute.waypointList }
    }
    val actionSender = createWaypointActionSender()
    val routeLifecycle = rememberWaypointRouteLifecycle(sideWaypointRoute)
    sideWaypointList.forEach { waypoint ->
        routeLifecycle.WithLifecycle(waypoint) {
            BackHandler {
                actionSender(BacktrackWaypointAction(waypoint.id))
            }
            waypoint.feature
                .getContent()
                .Content()
        }
    }
}

@Composable
fun SimpleWaypoint() {
    Box(
        modifier = Modifier
            .waypointHolder(listOf(Waypoint(feature = SimpleWaypointFeature())))
            .waypointActions {
                onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                    waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                        add(waypointAction.waypoint)
                    }
                }
                onAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
                    waypointHolder.updateWaypointList(WaypointNavigationType.Pop) {
                        removeIf { it.id == waypointAction.waypointId }
                    }
                }
            }
            .waypointRoutes {
                addRoute(::MainWaypointRoute)
                addRoute(::SideWaypointRoute)
            }
            .waypointScaffold {
                val waypointRouteProvider = ModifierLocalWaypointRouteProvider.current

                Navigation(waypointRouteProvider = waypointRouteProvider ?: return@waypointScaffold)
            },
        content = {},
    )
}
