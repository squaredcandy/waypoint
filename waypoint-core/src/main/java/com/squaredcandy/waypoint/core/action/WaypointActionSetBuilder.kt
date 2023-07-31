package com.squaredcandy.waypoint.core.action

import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlin.reflect.KClass

@DslMarker annotation class WaypointActionSetScope

@WaypointActionSetScope
class WaypointActionSetBuilder {
    private var actionResolvers = persistentHashMapOf<KClass<*>, WaypointActionResolver<*>>()

    fun <T: WaypointAction> onAction(waypointActionClass: KClass<T>, waypointActionResolver: WaypointActionResolver<T>) {
        actionResolvers = actionResolvers.put(waypointActionClass, waypointActionResolver)
    }

    internal fun build(): WaypointActionSet = WaypointActionSet(
        resolvers = actionResolvers.toImmutableMap(),
    )
}

inline fun <reified T: WaypointAction> WaypointActionSetBuilder.onAction(
    noinline block: (waypointHolder: MutableWaypointHolder, waypointAction: T) -> Unit,
) = onAction(T::class, waypointActionResolver<T>(block = block))

internal fun buildWaypointActions(
    builder: WaypointActionSetBuilder.() -> Unit,
): WaypointActionSet {
    return WaypointActionSetBuilder().apply(builder).build()
}
