package com.squaredcandy.waypoint.core.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.modifier.ModifierLocal

@Composable
fun <T> WaypointContentScope.rememberModifierLocalState(modifierLocal: ModifierLocal<T>): State<T?> = remember(this) {
    derivedStateOf { modifierLocal.currentOrNull }
}
