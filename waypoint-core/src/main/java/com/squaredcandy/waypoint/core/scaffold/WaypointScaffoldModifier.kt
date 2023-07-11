package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.modifierLocalConsumer

fun Modifier.waypointScaffold(
    waypointScaffoldDefinition: WaypointScaffoldDefinition,
) = this
    .then(WaypointScaffoldNodeElement(waypointScaffoldDefinition))
    .composed {
        var currentWaypointScaffoldDefinition by remember {
            mutableStateOf<WaypointScaffoldDefinition?>(null)
        }
        currentWaypointScaffoldDefinition?.Content()
        modifierLocalConsumer {
            currentWaypointScaffoldDefinition = ModifierLocalWaypointScaffoldDefinition.current
        }
    }

fun Modifier.waypointScaffold(
    content: @Composable WaypointScaffoldDefinition.() -> Unit,
) = composed { waypointScaffold(waypointScaffoldDefinition = waypointScaffoldDefinition(content)) }

@Composable
private fun waypointScaffoldDefinition(
    content: @Composable WaypointScaffoldDefinition.() -> Unit,
): WaypointScaffoldDefinition = remember {
    object : WaypointScaffoldDefinition() {
        @Composable
        override fun Content() = content()
    }
}
