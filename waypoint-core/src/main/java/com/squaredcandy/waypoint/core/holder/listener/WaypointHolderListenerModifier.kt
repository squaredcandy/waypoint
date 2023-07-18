package com.squaredcandy.waypoint.core.holder.listener

import androidx.compose.ui.Modifier

@ExperimentalWaypointHolderListenerModifier
fun Modifier.waypointHolderListener(): Modifier = this.then(WaypointHolderListenerNodeElement())
