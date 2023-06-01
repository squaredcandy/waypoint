package com.squaredcandy.waypoint.core.action

import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlin.reflect.KClass

@DslMarker annotation class WaypointActionMapScope

@WaypointActionMapScope
class WaypointActionMapBuilder {
    private var actionResolvers = persistentHashMapOf<KClass<*>, WaypointActionResolver<*>>()
    private var hooks = persistentListOf<WaypointActionHook>()

    fun <T: WaypointAction> onAction(waypointActionClass: KClass<T>, waypointActionResolver: WaypointActionResolver<T>) {
        actionResolvers = actionResolvers.put(waypointActionClass, waypointActionResolver)
    }

    fun addHook(hook: WaypointActionHook) {
        hooks = hooks.add(hook)
    }

    internal fun build(): WaypointActionMap = WaypointActionMap(
        resolvers = actionResolvers.toImmutableMap(),
        hooks = hooks.toImmutableList(),
    )
}

inline fun <reified T: WaypointAction> WaypointActionMapBuilder.onAction(
    noinline block: (waypointHolder: MutableWaypointHolder, waypointAction: T) -> Unit,
) = onAction(T::class, waypointActionResolver<T>(block = block))

fun WaypointActionMapBuilder.addHook(
    preResolveHook: (waypointHolder: WaypointHolder, waypointAction: WaypointAction) -> Unit,
    postResolveHook: (waypointHolder: WaypointHolder, waypointAction: WaypointAction) -> Unit,
) = addHook(
    hook = object : WaypointActionHook {
        override fun preResolveHook(
            waypointHolder: WaypointHolder,
            waypointAction: WaypointAction,
        ) = preResolveHook(waypointHolder, waypointAction)

        override fun postResolveHook(
            waypointHolder: WaypointHolder,
            waypointAction: WaypointAction,
        ) = postResolveHook(waypointHolder, waypointAction)
    }
)

internal fun buildWaypointActions(
    builder: WaypointActionMapBuilder.() -> Unit,
): WaypointActionMap {
    return WaypointActionMapBuilder().apply(builder).build()
}
