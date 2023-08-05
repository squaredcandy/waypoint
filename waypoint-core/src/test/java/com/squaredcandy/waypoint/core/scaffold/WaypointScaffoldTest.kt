package com.squaredcandy.waypoint.core.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.waypointHolder
import com.squaredcandy.waypoint.core.route.MainWaypointRoute
import com.squaredcandy.waypoint.core.route.ModifierLocalWaypointRouteProvider
import com.squaredcandy.waypoint.core.route.WaypointRouteProvider
import com.squaredcandy.waypoint.core.route.waypointRoutes
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN we have a waypoint holder WHEN we retrieve it in waypoint content THEN waypoint holder is not empty`() {
        val list = listOf(Waypoint())

        val waypointHolder: MutableState<MutableWaypointHolder?> = mutableStateOf(null)
        composeTestRule.setContent {
            WaypointScaffold(
                modifier = Modifier
                    .waypointHolder(list)
            ) {
                waypointHolder.value = ModifierLocalMutableWaypointHolder.current
            }
        }

        Truth.assertThat(waypointHolder.value?.waypointList).isNotEmpty()
    }

    @Test
    fun `GIVEN we have don't have a waypoint holder WHEN we retrieve it in waypoint content THEN waypoint holder is empty`() {
        val waypointHolder: MutableState<MutableWaypointHolder?> = mutableStateOf(null)
        composeTestRule.setContent {
            WaypointScaffold(modifier = Modifier) {
                waypointHolder.value = ModifierLocalMutableWaypointHolder.current
            }
        }

        Truth.assertThat(waypointHolder.value?.waypointList).isEmpty()
    }

    @Test
    fun `GIVEN waypoint route with main waypoint route WHEN getting the waypoint route provider THEN provider is null`() {
        var waypointRouteProvider: WaypointRouteProvider? = null
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointRoutes {
                        addRoute(::MainWaypointRoute)
                    }
                    .modifierLocalConsumer {
                        waypointRouteProvider = ModifierLocalWaypointRouteProvider.current
                    }
            )
        }

        Truth.assertThat(waypointRouteProvider).isNull()
    }
}
