package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WaypointScaffold(
    modifier: Modifier,
    content: @Composable WaypointScaffoldScope.() -> Unit,
) {
    Box(
        modifier = modifier.waypointScaffold(content),
        content = {},
    )
}
