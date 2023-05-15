package com.squaredcandy.waypoint

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.modifierLocalConsumer
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.ModifierLocalWaypointActionProvider
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.route.MainWaypointRoute
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.SideWaypointRoute
import com.squaredcandy.waypoint.core.route.WaypointRouteKey
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes

@Composable
private fun Navigation(
    waypointRouteProvider: WaypointRouteProvider,
    mutableWaypointHolder: MutableWaypointHolder,
    waypointActionProvider: WaypointActionProvider,
) {
    // TODO temporary fix, find a more long term solution
    val updatedWaypointActionProvider by rememberUpdatedState(newValue = waypointActionProvider)
    Box(modifier = Modifier.fillMaxSize()) {
        val mainWaypointList by remember {
            derivedStateOf {
                waypointRouteProvider.getRoute(WaypointRouteKey.main).waypointList
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
                waypointRouteProvider.getRoute(WaypointRouteKey.side).waypointList
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

@OptIn(ExperimentalComposeUiApi::class)
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
                addRoute(WaypointRouteKey.main, ::MainWaypointRoute)
                addRoute(WaypointRouteKey.side, ::SideWaypointRoute)
            }
            .composed {
                var mutableWaypointHolder by remember {
                    mutableStateOf<MutableWaypointHolder?>(null)
                }
                var waypointRouteProvider by remember {
                    mutableStateOf<WaypointRouteProvider?>(null)
                }
                var waypointActionProvider by remember {
                    mutableStateOf<WaypointActionProvider?>(null)
                }

                val modifier = modifierLocalConsumer {
                    mutableWaypointHolder = ModifierLocalMutableWaypointHolder.current
                    waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                    waypointActionProvider = ModifierLocalWaypointActionProvider.current
                }

                Navigation(
                    waypointRouteProvider = waypointRouteProvider ?: return@composed modifier,
                    mutableWaypointHolder = mutableWaypointHolder ?: return@composed modifier,
                    waypointActionProvider = waypointActionProvider ?: return@composed modifier,
                )

                modifier
            },
        content = {},
    )
}
