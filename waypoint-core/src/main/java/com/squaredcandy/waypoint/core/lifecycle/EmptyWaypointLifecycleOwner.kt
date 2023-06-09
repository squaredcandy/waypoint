package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.feature.WaypointContext
import com.squaredcandy.waypoint.core.route.WaypointRoute
import kotlin.reflect.KClass

class EmptyWaypointLifecycleOwner : WaypointLifecycleOwner {
    @Composable
    override fun WithLifecycle(
        waypoint: Waypoint,
        block: @Composable WaypointContext.() -> Unit,
    ) {
        val waypointContext = remember {
            object : WaypointContext {
                override val waypointId: Identifier<Waypoint> = waypoint.id

                override fun <T : WaypointAction> sendAction(
                    waypointActionClass: KClass<T>,
                    waypointAction: T,
                ): Result<Unit> {
                    return Result.failure(NotImplementedError("sendAction is not implemented"))
                }
            }
        }
        block(waypointContext)
    }
}
