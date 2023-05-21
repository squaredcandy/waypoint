package com.squaredcandy.waypoint

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.ModifierLocalWaypointActionProvider
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.content.rememberModifierLocalState
import com.squaredcandy.waypoint.core.content.waypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.route.MainWaypointRoute
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.SideWaypointRoute
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes

@Composable
private fun Navigation(
    mutableWaypointHolder: MutableWaypointHolder,
    waypointRouteProvider: WaypointRouteProvider,
    waypointActionProvider: WaypointActionProvider,
) {
    // TODO temporary fix, find a more long term solution
    val updatedWaypointActionProvider by rememberUpdatedState(newValue = waypointActionProvider)
    Box(modifier = Modifier.fillMaxSize()) {
        val mainWaypointList by remember {
            derivedStateOf {
                waypointRouteProvider.getRoute(MainWaypointRoute.key).waypointList
            }
        }
        if (mainWaypointList.isNotEmpty()) {
            val mainWaypoint by remember {
                derivedStateOf { mainWaypointList.last() }
            }
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = mainWaypoint,
                label = "main_waypoint",
            ) { waypoint ->
                val context by remember {
                    derivedStateOf {
                        WaypointContext(
                            waypointId = waypoint.id,
                            mutableWaypointHolder = mutableWaypointHolder,
                            waypointActionProvider = updatedWaypointActionProvider,
                        )
                    }
                }
                with(context) {
                    waypoint.feature
                        .getContent()
                        .Content()
                }
            }
        }

        val sideWaypointList by remember {
            derivedStateOf {
                waypointRouteProvider.getRoute(SideWaypointRoute.key).waypointList
            }
        }
        sideWaypointList.forEach { waypoint ->
            val context = WaypointContext(
                waypointId = waypoint.id,
                mutableWaypointHolder = mutableWaypointHolder,
                waypointActionProvider = waypointActionProvider,
            )
            with(context) {
                waypoint.feature
                    .getContent()
                    .Content()
            }
        }
    }
}

@Composable
fun Example() {
    Box(
        modifier = Modifier
            .waypointHolder(listOf(Waypoint(feature = ExampleWaypointFeature)))
            .waypointActions {
                onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                    waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                        add(waypointAction.waypoint)
                    }
                }
            }
            .waypointRoutes {
                addRoute(::MainWaypointRoute)
                addRoute(::SideWaypointRoute)
            }
            .waypointContent {
                val mutableWaypointHolder by rememberModifierLocalState(ModifierLocalMutableWaypointHolder)
                val waypointRouteProvider by rememberModifierLocalState(ModifierLocalWaypointRouteProvider)
                val waypointActionProvider by rememberModifierLocalState(ModifierLocalWaypointActionProvider)

                Navigation(
                    mutableWaypointHolder = mutableWaypointHolder ?: return@waypointContent,
                    waypointRouteProvider = waypointRouteProvider ?: return@waypointContent,
                    waypointActionProvider = waypointActionProvider ?: return@waypointContent,
                )
            },
        content = {},
    )
}
