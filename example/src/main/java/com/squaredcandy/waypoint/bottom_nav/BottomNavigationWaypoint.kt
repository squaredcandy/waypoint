package com.squaredcandy.waypoint.bottom_nav

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.squaredcandy.waypoint.bottom_nav.emails.EmailRepository
import com.squaredcandy.waypoint.bottom_nav.emails.LocalEmailRepository
import com.squaredcandy.waypoint.bottom_nav.new_emails.NewEmailsWaypointFeature
import com.squaredcandy.waypoint.bottom_nav.starred_emails.StarredEmailsWaypointFeature
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.WaypointTag
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.content.rememberModifierLocalState
import com.squaredcandy.waypoint.core.content.waypointContent
import com.squaredcandy.waypoint.core.feature.sendAction
import com.squaredcandy.waypoint.core.feature.transition.MaterialSharedAxisZScreenTransition
import com.squaredcandy.waypoint.core.feature.transition.WaypointTransition
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.lifecycle.ModifierLocalWaypointLifecycleOwner
import com.squaredcandy.waypoint.core.lifecycle.WaypointLifecycleOwner
import com.squaredcandy.waypoint.core.lifecycle.waypointLifecycle
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes
import com.squaredcandy.waypoint.util.getTransition
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.random.Random
import kotlin.random.nextLong

private val NewEmailsWaypointTag = WaypointTag("new_emails")
private val StarredEmailsWaypointTag = WaypointTag("starred_emails")

private fun indexToTag(index: Int): WaypointTag = when (index) {
    0 -> NewEmailsWaypointTag
    1 -> StarredEmailsWaypointTag
    else -> error("Invalid index")
}

private fun tagToIndex(tag: WaypointTag) = when (tag) {
    NewEmailsWaypointTag -> 0
    StarredEmailsWaypointTag -> 1
    else -> error("Invalid tag")
}

@Composable
fun BottomNavigationWaypoint() {
    val selectedTab = rememberSaveable {
        mutableStateOf(NewEmailsWaypointTag)
    }
    Box(
        modifier = Modifier
            .waypointHolder(
                listOf(
                    Waypoint(
                        feature = NewEmailsWaypointFeature(),
                        tags = setOf(
                            BottomNavigationWaypointRoute.BottomNavigationWaypointTag,
                            NewEmailsWaypointTag,
                        )
                    ),
                    Waypoint(
                        feature = StarredEmailsWaypointFeature(),
                        tags = setOf(
                            BottomNavigationWaypointRoute.BottomNavigationWaypointTag,
                            StarredEmailsWaypointTag,
                        )
                    ),
                ),
            )
            .waypointActions {
                onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                    waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                        add(
                            waypointAction.waypoint.copy(
                                tags = waypointAction.waypoint.tags.plus(selectedTab.value),
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
            .waypointLifecycle()
            .waypointContent {
                val waypointRouteProvider by rememberModifierLocalState(
                    ModifierLocalWaypointRouteProvider
                )
                val waypointLifecycleOwner by rememberModifierLocalState(
                    ModifierLocalWaypointLifecycleOwner
                )

                val emailRepository = rememberSaveable(
                    saver = EmailRepository.saver,
                ) {
                    EmailRepository()
                }
                LaunchedEffect(key1 = emailRepository) {
                    while (true) {
                        delay(Random.nextLong(3000L, 30000L))
                        emailRepository.addNewEmail()
                    }
                }

                CompositionLocalProvider(LocalEmailRepository provides emailRepository) {
                    Navigation(
                        selectedTab = selectedTab,
                        waypointRouteProvider = waypointRouteProvider ?: return@CompositionLocalProvider,
                        waypointLifecycleOwner = waypointLifecycleOwner ?: return@CompositionLocalProvider,
                    )
                }
            },
        content = {},
    )
}

@Composable
private fun Navigation(
    selectedTab: MutableState<WaypointTag>,
    waypointRouteProvider: WaypointRouteProvider,
    waypointLifecycleOwner: WaypointLifecycleOwner,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        BottomNavigationItemWaypointRoute(
            selectedTab = selectedTab,
            waypointRouteProvider = waypointRouteProvider,
            waypointLifecycleOwner = waypointLifecycleOwner,
            fallbackTransition = MaterialSharedAxisZScreenTransition,
        )

        NavigationBar {
            NavigationItem(
                selectedTab = selectedTab,
                tab = NewEmailsWaypointTag,
                selectedIcon = Icons.Filled.Email,
                unselectedIcon = Icons.Outlined.Email,
            )
            NavigationItem(
                selectedTab = selectedTab,
                tab = StarredEmailsWaypointTag,
                selectedIcon = Icons.Filled.Star,
                unselectedIcon = Icons.Outlined.Star,
            )
        }
    }
}

@Composable
fun RowScope.NavigationItem(
    selectedTab: MutableState<WaypointTag>,
    tab: WaypointTag,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
) {
    NavigationBarItem(
        selected = selectedTab.value == tab,
        onClick = {
            selectedTab.value = tab
        },
        icon = {
            Icon(
                imageVector = if (selectedTab.value == tab) selectedIcon else unselectedIcon,
                contentDescription = null,
            )
        },
    )
}

@Composable
private fun ColumnScope.BottomNavigationItemWaypointRoute(
    modifier: Modifier = Modifier,
    selectedTab: MutableState<WaypointTag>,
    waypointRouteProvider: WaypointRouteProvider,
    waypointLifecycleOwner: WaypointLifecycleOwner,
    fallbackTransition: WaypointTransition,
) {
    val bottomNavigationWaypointRoute by remember {
        derivedStateOf { waypointRouteProvider.getRoute(BottomNavigationWaypointRoute.key) }
    }
    val bottomNavigationWaypointList by remember {
        derivedStateOf { bottomNavigationWaypointRoute.waypointList }
    }
    val pagerState = rememberPagerState(0) { bottomNavigationWaypointList.size }
    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collectLatest { index -> selectedTab.value = indexToTag(index) }
    }
    LaunchedEffect(key1 = selectedTab.value, key2 = pagerState) {
        pagerState.animateScrollToPage(tagToIndex(selectedTab.value))
    }
    HorizontalPager(
        modifier = modifier.weight(1f),
        state = pagerState,
    ) { index ->
        val bottomNavigationItemWaypointRoute by remember {
            derivedStateOf { waypointRouteProvider.getRoute(BottomNavigationItemWaypointRoute.key) }
        }
        val indexState = rememberUpdatedState(newValue = index)
        val bottomNavigationItemWaypointList by remember {
            bottomNavigationItemWaypointRoute.waypointListOfTag(indexToTag(indexState.value))
        }

        val bottomNavigationWaypoint by remember {
            derivedStateOf { bottomNavigationItemWaypointList.last() }
        }
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = bottomNavigationWaypoint,
            label = "waypoint",
            transitionSpec = {
                getTransition(
                    waypointTransitionSpecType = bottomNavigationWaypointRoute.getWaypointTransitionSpecType(initialState),
                    fallbackTransition = fallbackTransition,
                )
            },
            contentKey = (Waypoint::id),
        ) { waypoint ->
            waypointLifecycleOwner.WithLifecycle(waypoint) {
                BackHandler(enabled = index != 0 || bottomNavigationItemWaypointList.size > 1) {
                    if (bottomNavigationItemWaypointList.size > 1) {
                        sendAction(BacktrackWaypointAction(waypointId))
                    } else if (index != 0) {
                        selectedTab.value = NewEmailsWaypointTag
                    }
                }
                waypoint.feature
                    .getContent()
                    .Content()
            }
        }
    }
}
