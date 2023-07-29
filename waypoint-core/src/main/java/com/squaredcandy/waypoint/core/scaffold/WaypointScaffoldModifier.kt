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
    waypointScaffoldContent: WaypointScaffoldContent,
) = this
    .then(WaypointScaffoldNodeElement(waypointScaffoldContent))
    .composed {
        var currentWaypointScaffoldContent by remember {
            mutableStateOf<WaypointScaffoldContent?>(null)
        }
        currentWaypointScaffoldContent?.apply { Content() }
        modifierLocalConsumer {
            currentWaypointScaffoldContent = ModifierLocalWaypointScaffoldContent.current
        }
    }

fun Modifier.waypointScaffold(
    content: @Composable WaypointScaffoldScope.() -> Unit,
) = composed {
    val waypointScaffoldContent = remember {
        object : WaypointScaffoldContent() {
            @Composable
            override fun WaypointScaffoldScope.Content() = content()
        }
    }
    waypointScaffold(waypointScaffoldContent)
}
