package com.squaredcandy.waypoint.bottom_nav.new_emails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.squaredcandy.waypoint.bottom_nav.emails.EmailLazyColumn
import com.squaredcandy.waypoint.bottom_nav.emails.LocalEmailRepository
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.LocalWaypointActionSender
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.invoke
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.util.formatDate
import com.squaredcandy.waypoint.util.rememberFunc

class NewEmailsWaypointContent : WaypointContent {
    @Composable
    override fun Content() {
        val actionSender = LocalWaypointActionSender.current
        val emailRepository = LocalEmailRepository.current
        val emailListState = emailRepository.emailListStateFlow.collectAsState()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val groupedEmailListState = remember {
            derivedStateOf { emailListState.value.groupBy { it.date.formatDate() } }
        }
        val updateEmailStarred = rememberFunc(
            key1 = emailRepository,
            method = emailRepository::updateEmailStarred,
        )
        val navigateTo = rememberFunc { waypoint: Waypoint ->
            actionSender(NavigateWaypointAction(waypoint))
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

            Box(modifier = Modifier.padding(it)) {
                EmailLazyColumn(
                    scrollBehavior = scrollBehavior,
                    groupedEmailListState = groupedEmailListState,
                    updateEmailStarred = updateEmailStarred,
                    navigateTo = navigateTo,
                )
            }
        }
    }
}
