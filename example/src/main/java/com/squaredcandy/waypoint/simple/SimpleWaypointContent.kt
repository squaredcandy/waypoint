package com.squaredcandy.waypoint.simple

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.BacktrackWaypointAction
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.transition.MaterialSharedAxisXScreenTransition
import com.squaredcandy.waypoint.core.action.LocalWaypointActionSender
import com.squaredcandy.waypoint.core.action.invoke
import com.squaredcandy.waypoint.core.route.LocalCanBacktrack
import com.squaredcandy.waypoint.core.route.LocalWaypoint
import com.squaredcandy.waypoint.util.rememberFunc
import kotlinx.coroutines.delay

class SimpleWaypointContent : WaypointContent {
    @Composable
    override fun Content() {
        val canBacktrack = LocalCanBacktrack.current
        val actionSender = LocalWaypointActionSender.current
        val waypoint = LocalWaypoint.current
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Test Title") },
                    navigationIcon = remember {
                        movableContentOf {
                            if (canBacktrack) {
                                IconButton(
                                    onClick = { actionSender(BacktrackWaypointAction(waypoint.id)) },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Go Back",
                                    )
                                }
                            }
                        }
                    },
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it),
            ) {
                var count by rememberSaveable { mutableIntStateOf(0) }
                Text(text = "Test $count")
                LaunchedEffect(key1 = Unit) {
                    repeat(100000) {
                        count++
                        delay(1000L)
                    }
                }
                val onClick = rememberFunc {
                    actionSender(
                        NavigateWaypointAction(
                            Waypoint(feature = SimpleWaypointFeature(MaterialSharedAxisXScreenTransition))
                        )
                    )
                }
                Button(
                    onClick = onClick,
                ) {
                    Text(text = "Next screen")
                }
            }
        }
    }
}
