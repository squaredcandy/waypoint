package com.squaredcandy.waypoint.bottom_nav.email_details

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.squaredcandy.waypoint.bottom_nav.emails.LocalEmailRepository
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.handle.DefaultWaypointHandle
import com.squaredcandy.waypoint.core.handle.rememberWaypointHandle
import com.squaredcandy.waypoint.core.handle.sendAction
import com.squaredcandy.waypoint.util.LoadingState
import com.squaredcandy.waypoint.util.collectAsMappedLoadingState
import com.squaredcandy.waypoint.util.getLoadedOrNull
import com.squaredcandy.waypoint.util.isLoaded
import com.squaredcandy.waypoint.util.mapTo
import com.squaredcandy.waypoint.util.rememberFunc

class EmailDetailsWaypointContent(private val emailId: String) : WaypointContent {

    @Immutable
    private data class EmailMessage(
        val from: String,
        val to: String,
        val title: String,
        val message: String,
    )

    context(WaypointContext)
    @Composable
    override fun Content() {
        val handle = rememberWaypointHandle(::DefaultWaypointHandle)
        val emailRepository = LocalEmailRepository.current
        val emailLoadingState = emailRepository.emailListStateFlow
            .collectAsMappedLoadingState { emailList -> emailList.find { it.id == emailId } }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        val titleState = remember {
                            derivedStateOf { emailLoadingState.value.getLoadedOrNull()?.title }
                        }
                        titleState.value?.let { title ->
                            Text(
                                text = title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    },
                    navigationIcon = {
                        val backButton: () -> Unit = rememberFunc(waypointId) {
                            handle.sendAction(BacktrackWaypointAction(waypointId))
                        }
                        IconButton(onClick = backButton) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        val starredState = remember {
                            derivedStateOf { emailLoadingState.value.getLoadedOrNull()?.starred }
                        }
                        val onEmailStarClicked = rememberFunc {
                            emailRepository.updateEmailStarred(emailId)
                        }
                        starredState.value?.let { starred ->
                            IconButton(onClick = onEmailStarClicked) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = if (starred) "Unstar Email" else "Star Email",
                                    tint = if (starred) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                                )
                            }
                        }
                    },
                )
            },
        ) { paddingValues ->
            val emailMessageLoadingState = remember {
                derivedStateOf {
                    emailLoadingState.value.mapTo { email ->
                        EmailMessage(
                            from = email.from,
                            to = email.to,
                            title = email.title,
                            message = email.message,
                        )
                    }
                }
            }
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                targetState = emailMessageLoadingState.value,
                label = "email_state",
                contentKey = LoadingState<EmailMessage>::isLoaded,
            ) { emailLoadingState ->
                when (val state = emailLoadingState) {
                    is LoadingState.Loaded -> {
                        LoadedEmail(
                            from = state.item.from,
                            to = state.item.to,
                            title = state.item.title,
                            message = state.item.message,
                        )
                    }
                    is LoadingState.Loading -> LoadingEmail()
                }
            }
        }
    }

    @Composable
    private fun LoadedEmail(
        from: String,
        to: String,
        title: String,
        message: String,
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "From: $from",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "To: $to",
                style = MaterialTheme.typography.bodyLarge,
            )
            Divider()
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }

    @Composable
    private fun LoadingEmail() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
    }
}
