package com.squaredcandy.waypoint.bottom_nav

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.squaredcandy.waypoint.core.WaypointTag
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.scaffold.waypointScaffold
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.lifecycle.ModifierLocalWaypointLifecycleOwner
import com.squaredcandy.waypoint.core.lifecycle.WaypointLifecycleOwner
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.util.rememberFunc
import kotlinx.coroutines.flow.collectLatest

object BottomNavigationWaypointContent : WaypointContent {

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

    context(WaypointContext)
    @Composable
    override fun Content() {
        val selectedTab = rememberSaveable {
            mutableStateOf(NewEmailsWaypointTag)
        }
        Box(
            modifier = Modifier
                .waypointScaffold {
                    val waypointRouteProviderState = ModifierLocalWaypointRouteProvider.current
                    val waypointLifecycleOwnerState = ModifierLocalWaypointLifecycleOwner.current
                    Column {
                        BottomNavigationItemWaypointRoute(
                            selectedTab = selectedTab,
                            waypointRouteProvider = waypointRouteProviderState
                                ?: return@waypointScaffold,
                            waypointLifecycleOwner = waypointLifecycleOwnerState,
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
        ) {}
    }

    @Composable
    private fun RowScope.NavigationItem(
        selectedTab: MutableState<WaypointTag>,
        tab: WaypointTag,
        selectedIcon: ImageVector,
        unselectedIcon: ImageVector,
    ) {
        val isSelected by remember { derivedStateOf { selectedTab.value == tab } }
        val onSelectTab = rememberFunc { selectedTab.value = tab }
        NavigationBarItem(
            selected = isSelected,
            onClick = onSelectTab,
            icon = {
                Icon(
                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
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
    ) {
        val bottomNavigationItemWaypointRoute by remember {
            derivedStateOf { waypointRouteProvider.getRoute(BottomNavigationItemWaypointRoute.key) }
        }
        val bottomNavigationWaypointList by remember {
            derivedStateOf { bottomNavigationItemWaypointRoute.waypointList }
        }
        val pagerState = rememberPagerState(tagToIndex(selectedTab.value)) { bottomNavigationWaypointList.size }
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
            val waypoint by remember {
                derivedStateOf { bottomNavigationWaypointList[index] }
            }
            waypointLifecycleOwner.WithLifecycle(waypoint) {
                BackHandler(enabled = index != 0) {
                    selectedTab.value = NewEmailsWaypointTag
                }
                waypoint.feature
                    .getContent()
                    .Content()
            }
        }
    }
}
