package com.squaredcandy.waypoint.core.handle

import androidx.compose.ui.Modifier

fun Modifier.waypointHandleProvider(): Modifier = this.then(WaypointHandleProviderNodeElement())
