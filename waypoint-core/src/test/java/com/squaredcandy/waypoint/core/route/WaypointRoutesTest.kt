package com.squaredcandy.waypoint.core.route

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.semantics.assertWaypointListEqualTo
import com.squaredcandy.waypoint.core.semantics.assertWaypointRouteDoesNotExist
import com.squaredcandy.waypoint.core.semantics.assertWaypointRouteEqualTo
import com.squaredcandy.waypoint.core.semantics.assertWaypointRouteExists
import com.squaredcandy.waypoint.core.semantics.invokeWaypointAction
import com.squaredcandy.waypoint.core.semantics.onWaypointRouteNode
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointRoutesTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN waypoint is added THEN parent waypoint list is updated AND waypoint route is updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointActions {
                        onAction<NavigateWaypointAction> { waypointHolder, waypointAction ->
                            waypointHolder.updateWaypointList(WaypointNavigationType.Push) {
                                add(waypointAction.waypoint)
                            }
                        }
                    }
                    .waypointRoutes {
                        addRoute(WaypointRouteKey.main, ::MainWaypointRoute)
                    }
            )
        }

        composeTestRule.onWaypointRouteNode()
            .assertWaypointRouteExists(WaypointRouteKey.main)
            .assertWaypointRouteEqualTo(WaypointRouteKey.main, list)
            .invokeWaypointAction<NavigateWaypointAction>(NavigateWaypointAction(newWaypoint))
            .assertWaypointListEqualTo(list + newWaypoint)
            .assertWaypointRouteEqualTo(WaypointRouteKey.main, listOf(newWaypoint))
    }

    @Test
    fun `GIVEN waypoint route key does not exist WHEN getting route with route key THEN error is thrown`() {
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(emptyList())
                    .waypointRoutes {
                        addRoute(WaypointRouteKey.main, ::MainWaypointRoute)
                    }
            )
        }

        composeTestRule.onWaypointRouteNode()
            .assertWaypointRouteDoesNotExist(WaypointRouteKey.side)
    }
}
