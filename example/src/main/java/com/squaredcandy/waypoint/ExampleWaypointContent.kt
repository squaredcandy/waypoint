package com.squaredcandy.waypoint

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.feature.WaypointContent
import com.squaredcandy.waypoint.core.feature.sendAction
import com.squaredcandy.waypoint.core.feature.WaypointContext

class ExampleWaypointContent : WaypointContent {
    context(WaypointContext)
    @Composable
    override fun Content() {
        BackHandler(enabled = canBacktrack) {
            sendAction(BacktrackWaypointAction(waypointId))
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { Text(text = "Test Title") })
            }
        ) {
            Column(
                modifier = Modifier.padding(it),
            ) {
                Text(text = "Test")
                Button(
                    onClick = {
                        sendAction(NavigateWaypointAction(Waypoint(feature = ExampleWaypointFeature)))
                    },
                ) {
                    Text(text = "Next ${waypointId.id}")
                }
            }
        }
    }
}
