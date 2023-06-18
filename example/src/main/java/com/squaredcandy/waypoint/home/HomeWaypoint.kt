package com.squaredcandy.waypoint.home

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.squaredcandy.waypoint.bottom_nav.BottomNavigationActivity
import com.squaredcandy.waypoint.simple.SimpleActivity

@Composable
fun HomeWaypoint(activity: Activity) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Waypoint")
                },
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            Card(
                onClick = {
                    activity.startActivity(Intent(activity, SimpleActivity::class.java))
                },
                modifier = Modifier.padding(8.dp),
            ) {
                ListItem(
                    headlineContent = {
                        Text(text = "Simple Example")
                    },
                )
            }

            Card(
                onClick = {
                    activity.startActivity(Intent(activity, BottomNavigationActivity::class.java))
                },
                modifier = Modifier.padding(8.dp),
            ) {
                ListItem(
                    headlineContent = {
                        Text(text = "Navigation Bar Example")
                    },
                )
            }
        }
    }
}
