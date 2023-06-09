package com.squaredcandy.waypoint.core.feature

import androidx.compose.runtime.mutableStateOf
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.action.WaypointActionProvider
import com.squaredcandy.waypoint.core.action.WaypointActionResolver
import com.squaredcandy.waypoint.core.action.actions.NavigateWaypointAction
import com.squaredcandy.waypoint.core.action.noOpWaypointActionProvider
import com.squaredcandy.waypoint.core.holder.DefaultWaypointHolder
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.reflect.KClass

class RealWaypointContextTest {

    @Test
    fun `GIVEN an action provider WHEN sending an action THEN a success is returned`() = runTest {
        val waypoint = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf())
        val waypointActionProvider = object : WaypointActionProvider {
            override fun <T : WaypointAction> getAction(
                waypointActionClass: KClass<T>
            ): WaypointActionResolver<T>? {
                return if (waypointActionClass == NavigateWaypointAction::class) {
                    WaypointActionResolver { _, _ -> }
                } else {
                    null
                }
            }
        }

        val waypointHolderState = mutableStateOf(waypointHolder)
        val waypointActionProviderState = mutableStateOf(waypointActionProvider)

        val waypointContext = RealWaypointContext(
            waypoint = waypoint,
            mutableWaypointHolderState = waypointHolderState,
            waypointActionProviderState = waypointActionProviderState,
        )

        val result = with (waypointContext) {
            sendAction<NavigateWaypointAction>(NavigateWaypointAction(waypoint))
        }

        Truth.assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `GIVEN a no op action provider WHEN sending an action THEN a failure is returned`() = runTest {
        val waypoint = Waypoint()
        val waypointHolder = DefaultWaypointHolder(mutableListOf())
        val waypointActionProvider = noOpWaypointActionProvider

        val waypointHolderState = mutableStateOf(waypointHolder)
        val waypointActionProviderState = mutableStateOf(waypointActionProvider)

        val waypointContext = RealWaypointContext(
            waypoint = waypoint,
            mutableWaypointHolderState = waypointHolderState,
            waypointActionProviderState = waypointActionProviderState,
        )

        val result = with (waypointContext) {
            sendAction<NavigateWaypointAction>(NavigateWaypointAction(waypoint))
        }

        Truth.assertThat(result.isFailure).isTrue()
    }
}
