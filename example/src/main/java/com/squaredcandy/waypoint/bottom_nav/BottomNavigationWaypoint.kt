package com.squaredcandy.waypoint.bottom_nav

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.squaredcandy.waypoint.bottom_nav.emails.EmailRepository
import com.squaredcandy.waypoint.bottom_nav.emails.LocalEmailRepository
import com.squaredcandy.waypoint.bottom_nav.new_emails.NewEmailsWaypointFeature
import com.squaredcandy.waypoint.bottom_nav.starred_emails.StarredEmailsWaypointFeature
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.scaffold.waypointScaffold
import com.squaredcandy.waypoint.core.feature.transition.MaterialSharedAxisZScreenTransition
import com.squaredcandy.waypoint.core.handle.DefaultWaypointHandle
import com.squaredcandy.waypoint.core.handle.LocalWaypoint
import com.squaredcandy.waypoint.core.handle.LocalWaypointHandleProvider
import com.squaredcandy.waypoint.core.handle.ModifierLocalWaypointHandleProvider
import com.squaredcandy.waypoint.core.handle.rememberWaypointHandle
import com.squaredcandy.waypoint.core.handle.sendAction
import com.squaredcandy.waypoint.core.handle.waypointHandleProvider
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.lifecycle.ModifierLocalWaypointLifecycleOwner
import com.squaredcandy.waypoint.core.lifecycle.WaypointLifecycleOwner
import com.squaredcandy.waypoint.core.lifecycle.waypointLifecycle
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes
import com.squaredcandy.waypoint.util.getTransition

@Composable
fun BottomNavigationWaypoint() {
    Box(
        modifier = Modifier
            .waypointHolder(
                listOf(
                    Waypoint(
                        feature = NewEmailsWaypointFeature(),
                        tags = setOf(BottomNavigationItemWaypointRoute.BottomNavigationItemWaypointTag),
                    ),
                    Waypoint(
                        feature = StarredEmailsWaypointFeature(),
                        tags = setOf(BottomNavigationItemWaypointRoute.BottomNavigationItemWaypointTag),
                    ),
                    Waypoint(
                        feature = BottomNavigationWaypointFeature,
                        tags = setOf(BottomNavigationWaypointRoute.BottomNavigationWaypointTag),
                    ),
                )
            )
            .waypointActions {
                onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                    waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                        add(
                            waypointAction.waypoint.copy(
                                tags = waypointAction.waypoint.tags
                                    .plus(BottomNavigationWaypointRoute.BottomNavigationWaypointTag),
                            )
                        )
                    }
                }
                onAction<BacktrackWaypointAction> { waypointHolder, waypointAction ->
                    waypointHolder.updateWaypointList(WaypointNavigationType.Pop) {
                        removeIf { it.id == waypointAction.waypointId }
                    }
                }
            }
            .waypointRoutes {
                addRoute(::BottomNavigationWaypointRoute)
                addRoute(::BottomNavigationItemWaypointRoute)
            }
            .waypointHandleProvider()
            .waypointLifecycle()
            .waypointScaffold {
                val waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                val waypointLifecycleOwner = ModifierLocalWaypointLifecycleOwner.current
                val waypointHandleProvider = ModifierLocalWaypointHandleProvider.current

                val emailRepository = rememberSaveable(
                    saver = EmailRepository.saver,
                ) {
                    EmailRepository()
                        .apply { addNewEmails(10) }
                }

                CompositionLocalProvider(
                    LocalEmailRepository provides emailRepository,
                    LocalWaypointHandleProvider provides waypointHandleProvider,
                ) {
                    Navigation(
                        waypointRouteProvider = waypointRouteProvider
                            ?: return@CompositionLocalProvider,
                        waypointLifecycleOwner = waypointLifecycleOwner,
                    )
                }
            },
        content = {},
    )
}

@Composable
private fun Navigation(
    waypointRouteProvider: WaypointRouteProvider,
    waypointLifecycleOwner: WaypointLifecycleOwner,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        val bottomNavigationWaypointRoute by remember {
            derivedStateOf { waypointRouteProvider.getRoute(BottomNavigationWaypointRoute.key) }
        }
        val bottomNavigationWaypointList by remember {
            derivedStateOf { bottomNavigationWaypointRoute.waypointList }
        }
        val bottomNavigationWaypoint by remember {
            derivedStateOf { bottomNavigationWaypointList.last() }
        }

        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = bottomNavigationWaypoint,
            label = "waypoint",
            transitionSpec = {
                getTransition(
                    waypointTransitionSpecType = bottomNavigationWaypointRoute.getWaypointTransitionSpecType(initialState),
                    fallbackTransition = MaterialSharedAxisZScreenTransition,
                )
            },
            contentKey = (Waypoint::id),
        ) { waypoint ->
            waypointLifecycleOwner.WithLifecycle(waypoint) {
                CompositionLocalProvider(LocalWaypoint provides waypoint) {
                    val handle = rememberWaypointHandle(::DefaultWaypointHandle)
                    BackHandler(bottomNavigationWaypointRoute.canBacktrack) {
                        handle.sendAction(BacktrackWaypointAction(waypointId))
                    }
                    waypoint.feature
                        .getContent()
                        .Content()
                }
            }
        }
    }
}
