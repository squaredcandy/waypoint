package com.squaredcandy.waypoint.core.contentV2

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.modifierLocalConsumer

fun Modifier.waypointContent(
    waypointContentDefinition: WaypointContentDefinition,
) = this
    .then(WaypointContentNodeElement(waypointContentDefinition))
    .composed {
        var currentWaypointContentDefinition by remember {
            mutableStateOf<WaypointContentDefinition?>(null)
        }
        currentWaypointContentDefinition?.Content()
        modifierLocalConsumer {
            currentWaypointContentDefinition = ModifierLocalWaypointContentDefinition.current
        }
    }

fun Modifier.waypointContent(
    content: @Composable WaypointContentDefinition.() -> Unit,
) = composed { waypointContent(waypointContentDefinition = waypointContentDefinition(content)) }

@Composable
private fun waypointContentDefinition(
    content: @Composable WaypointContentDefinition.() -> Unit,
): WaypointContentDefinition = remember {
    object : WaypointContentDefinition() {
        @Composable
        override fun Content() = content()
    }
}
