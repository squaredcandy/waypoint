package com.squaredcandy.waypoint.bottom_nav.new_emails

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squaredcandy.waypoint.bottom_nav.emails.Email
import com.squaredcandy.waypoint.bottom_nav.emails.LocalEmailRepository
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

class NewEmailsWaypointContent : WaypointContent {
    context(WaypointContext)
    @Composable
    override fun Content() {
        val emailRepository = LocalEmailRepository.current
        val emailListState = emailRepository.emailListStateFlow.collectAsState()
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        val groupedEmailListState = remember {
            derivedStateOf { emailListState.value.groupBy(Email::date) }
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
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
            ) {
                groupedEmailListState.value.forEach { (key, list) ->
                    stickyHeader(
                        key = key,
                        contentType = "data_header",
                    ) {
                        Surface {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp),
                                text = key.formatDate(),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                    items(
                        items = list,
                        key = Email::id,
                        contentType = { "email_item" },
                    ) { email ->
                        val onEmailStarClicked = remember(email) {
                            { emailRepository.updateEmailStarred(email.id) }
                        }
                        EmailItem(
                            email = email,
                            onEmailStarClicked = onEmailStarClicked,
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun LazyItemScope.EmailItem(email: Email, onEmailStarClicked: () -> Unit) {
        Card(
            modifier = Modifier
                .animateItemPlacement()
                .padding(8.dp),
            onClick = {},
        ) {
            ListItem(
                leadingContent = {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        tonalElevation = 2.dp,
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            text = email.from.firstOrNull()?.uppercase() ?: "",
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                },
                headlineContent = {
                    Text(text = email.from)
                },
                supportingContent = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = email.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                trailingContent = {
                    IconButton(onClick = onEmailStarClicked) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = if (email.starred) "Unstar Email" else "Star Email",
                            tint = if (email.starred) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                        )
                    }
                },
            )
        }
    }

    private fun ZonedDateTime.formatDate(): String {
        val currentDate = ZonedDateTime.now()
        return when (ChronoUnit.DAYS.between(currentDate, this)) {
            0L -> "Today"
            1L -> "Yesterday"
            else -> this.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
        }
    }
}
