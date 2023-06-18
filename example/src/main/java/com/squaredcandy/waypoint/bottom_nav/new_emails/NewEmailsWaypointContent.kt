package com.squaredcandy.waypoint.bottom_nav.new_emails

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.content.WaypointContent
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.feature.sendAction
import com.squaredcandy.waypoint.simple.SimpleWaypointFeature

class NewEmailsWaypointContent : WaypointContent {
    context(WaypointContext)
    @Composable
    override fun Content() {
        Column {
            Text(text = "New Emails")
            Button(
                onClick = {
                    sendAction(NavigateWaypointAction(Waypoint(feature = SimpleWaypointFeature())))
                }
            ) {
                Text(text = "New waypoint")
            }
        }
    }
}
