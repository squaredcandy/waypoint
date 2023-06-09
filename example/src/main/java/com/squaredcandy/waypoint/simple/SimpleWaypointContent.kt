package com.squaredcandy.waypoint.simple

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.feature.sendAction
import com.squaredcandy.waypoint.core.feature.transition.MaterialSharedAxisXScreenTransition
import com.squaredcandy.waypoint.util.rememberFunc
import kotlinx.coroutines.delay

class SimpleWaypointContent : WaypointContent {
    context(WaypointContext)
    @Composable
    override fun Content() {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Test Title") },
//                    navigationIcon = remember {
//                        movableContentOf {
//                            IconButton(
//                                onClick = { sendAction(BacktrackWaypointAction(waypointId)) },
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.ArrowBack,
//                                    contentDescription = "Go Back",
//                                )
//                            }
//                        }
//                    },
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
                    sendAction(
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
