package com.squaredcandy.waypoint.core.route.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.modifier.ModifierLocalReadScope
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import com.squaredcandy.waypoint.core.scaffold.WaypointScaffoldScope

interface WaypointRouteLifecycle {
    context(ModifierLocalReadScope)
    @Composable
    fun WithLifecycle(
        waypoint: Waypoint,
        vararg additionalProviders: ProvidedValue<*>,
        content: @Composable () -> Unit,
    )
}

@Composable
fun WaypointScaffoldScope.rememberWaypointRouteLifecycle(
    waypointRouteLifecycleProvider: WaypointRouteLifecycleProvider,
): WaypointRouteLifecycle {
    val saveableStateHolder = rememberSaveableStateHolder()
    val coroutineScope = rememberCoroutineScope()

    return remember(saveableStateHolder, coroutineScope) {
        RealWaypointRouteLifecycle(
            saveableStateHolder = saveableStateHolder,
            waypointRouteLifecycleProvider = waypointRouteLifecycleProvider,
            waypointHolder = ModifierLocalWaypointHolder.current,
            coroutineScope = coroutineScope,
        )
    }
}
