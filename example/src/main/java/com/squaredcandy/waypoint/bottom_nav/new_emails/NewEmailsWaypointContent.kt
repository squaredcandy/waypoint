package com.squaredcandy.waypoint.bottom_nav.new_emails

import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.squaredcandy.waypoint.bottom_nav.emails.EmailLazyColumn
import com.squaredcandy.waypoint.bottom_nav.emails.LocalEmailRepository
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.util.formatDate

class NewEmailsWaypointContent : WaypointContent {
    context(WaypointContext)
    @Composable
    override fun Content() {
        val emailRepository = LocalEmailRepository.current
        val emailListState = emailRepository.emailListStateFlow.collectAsState()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val groupedEmailListState = remember {
            derivedStateOf { emailListState.value.groupBy { it.date.formatDate() } }
        }

        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(text = "Emails")
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
        ) {
            EmailLazyColumn(
                paddingValues = it,
                nestedScrollConnection = scrollBehavior.nestedScrollConnection,
                emailRepository = emailRepository,
                groupedEmailListState = groupedEmailListState,
            )
        }
    }
}
