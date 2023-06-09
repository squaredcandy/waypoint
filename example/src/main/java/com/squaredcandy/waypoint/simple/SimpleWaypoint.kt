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
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.scaffold.waypointScaffold
import com.squaredcandy.waypoint.core.feature.sendAction
import com.squaredcandy.waypoint.core.feature.transition.DefaultScreenTransition
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.lifecycle.ModifierLocalWaypointLifecycleOwner
import com.squaredcandy.waypoint.core.lifecycle.WaypointLifecycleOwner
import com.squaredcandy.waypoint.core.lifecycle.waypointLifecycle
import com.squaredcandy.waypoint.core.route.MainWaypointRoute
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.SideWaypointRoute
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes
import com.squaredcandy.waypoint.util.getTransition

@Composable
private fun Navigation(
    waypointRouteProvider: WaypointRouteProvider,
    waypointLifecycleOwner: WaypointLifecycleOwner,
    fallbackTransition: WaypointTransition = DefaultScreenTransition,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        MainWaypointRoute(
            waypointRouteProvider = waypointRouteProvider,
            waypointLifecycleOwner = waypointLifecycleOwner,
            fallbackTransition = fallbackTransition,
        )

        SideWaypointRoute(
            waypointRouteProvider = waypointRouteProvider,
            waypointLifecycleOwner = waypointLifecycleOwner,
        )
    }
}

@Composable
private fun MainWaypointRoute(
    waypointRouteProvider: WaypointRouteProvider,
    waypointLifecycleOwner: WaypointLifecycleOwner,
    fallbackTransition: WaypointTransition,
) {
    val mainWaypointRoute by remember {
        derivedStateOf { waypointRouteProvider.getRoute(MainWaypointRoute.key) }
    }
    val mainWaypointList by remember {
        derivedStateOf { mainWaypointRoute.waypointList }
    }

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
            waypointLifecycleOwner.WithLifecycle(waypoint) {
                BackHandler(enabled = mainWaypointRoute.canBacktrack) {
                    sendAction(BacktrackWaypointAction(waypointId))
                }
                waypoint.feature
                    .getContent()
                    .Content()
            }
        }
    }
}

@Composable
private fun SideWaypointRoute(
    waypointRouteProvider: WaypointRouteProvider,
    waypointLifecycleOwner: WaypointLifecycleOwner,
) {
    val sideWaypointRoute by remember {
        derivedStateOf { waypointRouteProvider.getRoute(SideWaypointRoute.key) }
    }
    val sideWaypointList by remember {
        derivedStateOf { sideWaypointRoute.waypointList }
    }
    sideWaypointList.forEach { waypoint ->
        waypointLifecycleOwner.WithLifecycle(waypoint) {
            BackHandler {
                sendAction(BacktrackWaypointAction(waypointId))
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
            .waypointLifecycle()
            .waypointScaffold {
                val waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                val waypointLifecycleOwner = ModifierLocalWaypointLifecycleOwner.current

                Navigation(
                    waypointRouteProvider = waypointRouteProvider ?: return@waypointScaffold,
                    waypointLifecycleOwner = waypointLifecycleOwner,
                )
            },
        content = {},
    )
}
