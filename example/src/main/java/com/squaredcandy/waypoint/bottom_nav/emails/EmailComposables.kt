package com.squaredcandy.waypoint.bottom_nav.emails

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.squaredcandy.waypoint.bottom_nav.email_details.EmailDetailsWaypointFeature
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.feature.sendAction

context(WaypointContext)
@Composable
internal fun EmailLazyColumn(
    paddingValues: PaddingValues,
    nestedScrollConnection: NestedScrollConnection,
    emailRepository: EmailRepository,
    groupedEmailListState: State<Map<String, List<Email>>>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .nestedScroll(nestedScrollConnection),
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
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp),
                        text = key,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
            items(
                items = list,
                key = Email::id,
                contentType = { "email_item" },
            ) { email ->
                val onEmailClicked: () -> Unit = remember(email) {
                    {
                        sendAction(
                            NavigateWaypointAction(
                                Waypoint(feature = EmailDetailsWaypointFeature(email.id))
                            )
                        )
                    }
                }
                val onEmailStarClicked = remember(email) {
                    { emailRepository.updateEmailStarred(email.id) }
                }
                EmailItem(
                    email = email,
                    onEmailClicked = onEmailClicked,
                    onEmailStarClicked = onEmailStarClicked,
                )
            }
        }
    }
}

@Composable
internal fun LazyItemScope.EmailItem(
    email: Email,
    onEmailClicked: () -> Unit,
    onEmailStarClicked: () -> Unit,
) {
    Card(
        modifier = Modifier
            .animateItemPlacement()
            .padding(8.dp),
        onClick = onEmailClicked,
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
