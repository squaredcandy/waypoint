package com.squaredcandy.waypoint.core.holder.listener

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
@ExperimentalWaypointHolderListenerModifier
fun WaypointHolderListenerManager.OnWaypointHolderChanged(listener: WaypointHolderListener) {
    DisposableEffect(key1 = this, key2 = listener) {
        registerOnWaypointHolderChanged(listener)
        onDispose {
            deregisterOnWaypointHolderChanged(listener)
        }
    }
}
