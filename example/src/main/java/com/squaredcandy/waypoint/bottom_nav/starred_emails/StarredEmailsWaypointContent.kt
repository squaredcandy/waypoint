package com.squaredcandy.waypoint.bottom_nav.starred_emails

import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.squaredcandy.waypoint.bottom_nav.emails.Email
import com.squaredcandy.waypoint.bottom_nav.emails.EmailLazyColumn
import com.squaredcandy.waypoint.bottom_nav.emails.LocalEmailRepository
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.util.formatDate

class StarredEmailsWaypointContent : WaypointContent {
    context(WaypointContext)
    @Composable
    override fun Content() {
        val emailRepository = LocalEmailRepository.current
        val emailListState = emailRepository.emailListStateFlow.collectAsState()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val groupedStarredEmailListState = remember {
            derivedStateOf {
                emailListState.value
                    .filter(Email::starred)
                    .groupBy { it.date.formatDate() }
            }
        }

        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(text = "Starred Emails")
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {
            EmailLazyColumn(
                paddingValues = it,
                nestedScrollConnection = scrollBehavior.nestedScrollConnection,
                emailRepository = emailRepository,
                groupedEmailListState = groupedStarredEmailListState,
            )
        }
    }
}
