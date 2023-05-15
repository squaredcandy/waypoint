package com.squaredcandy.waypoint.core.route

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.ModifierLocalWaypointActionProvider
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.onAction
import com.squaredcandy.waypoint.core.action.getAction
import com.squaredcandy.waypoint.core.action.waypointActions
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.holder.waypointHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalComposeUiApi::class)
class WaypointRoutesTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `WHEN waypoint is added THEN parent waypoint list is updated AND waypoint route is updated`() {
        val list = listOf(Waypoint())
        val newWaypoint = Waypoint()

        var waypointHolder: MutableWaypointHolder? = null
        var waypointActionProvider: WaypointActionProvider? = null
        var waypointRouteProvider: WaypointRouteProvider? = null
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
                    .modifierLocalConsumer {
                        waypointHolder = ModifierLocalMutableWaypointHolder.current
                        waypointActionProvider = ModifierLocalWaypointActionProvider.current
                        waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                    }
            )
        }

        Truth.assertThat(waypointHolder).isNotNull()
        Truth.assertThat(waypointActionProvider).isNotNull()
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list)
        Truth.assertThat(waypointRouteProvider?.getRoute(WaypointRouteKey.main)?.waypointList).isEqualTo(list)
        val waypointAction = waypointActionProvider?.getAction<NavigateWaypointAction>()
        waypointAction?.invoke(waypointHolder!!, NavigateWaypointAction(newWaypoint))
        Truth.assertThat(waypointHolder?.waypointList).isEqualTo(list + newWaypoint)
        Truth.assertThat(waypointRouteProvider?.getRoute(WaypointRouteKey.main)?.waypointList).isEqualTo(listOf(newWaypoint))
    }

    @Test
    fun `GIVEN waypoint route key does not exist WHEN getting route with route key THEN error is thrown`() {
        var waypointRouteProvider: WaypointRouteProvider? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(emptyList())
                    .waypointRoutes {
                        addRoute(WaypointRouteKey.main, ::MainWaypointRoute)
                    }
                    .modifierLocalConsumer {
                        waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                    }
            )
        }

        Truth.assertThat(waypointRouteProvider).isNotNull()
        val result = runCatching {
            waypointRouteProvider?.getRoute(WaypointRouteKey.side)
        }
        Truth.assertThat(result.isFailure).isTrue()
    }
}
