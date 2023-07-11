package com.squaredcandy.waypoint.core.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.modifierLocalConsumer

@Deprecated("Use com.squaredcandy.waypoint.core.contentV2.waypointContent instead")
fun Modifier.waypointContent(
    content: @Composable WaypointContentScope.() -> Unit,
): Modifier = composed {
    var modifierLocalReadScope by remember {
        mutableStateOf<WaypointContentScope>(EmptyWaypointContentScope)
    }
    with(modifierLocalReadScope) {
        content()
    }

    modifierLocalConsumer {
        modifierLocalReadScope = RealWaypointContentScope(this)
    }
}
