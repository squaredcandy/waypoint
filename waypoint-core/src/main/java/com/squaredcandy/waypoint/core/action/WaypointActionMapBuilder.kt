package com.squaredcandy.waypoint.core.action

import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlin.reflect.KClass

@DslMarker annotation class WaypointActionMapScope

@WaypointActionMapScope
class WaypointActionMapBuilder {
    private var actionResolvers = persistentHashMapOf<KClass<*>, WaypointActionResolver>()
    private var hooks = persistentHashMapOf<KClass<*>, ImmutableList<WaypointActionHook>>()

    fun <T: WaypointAction> onAction(waypointActionClass: KClass<T>, waypointActionResolver: WaypointActionResolver) {
        actionResolvers = actionResolvers.put(waypointActionClass, waypointActionResolver)
    }

    fun <T: WaypointAction> addHook(waypointActionClass: KClass<T>, hook: WaypointActionHook) {
        val hookList = hooks.getOrDefault(waypointActionClass, persistentListOf())
        hooks = hooks.put(waypointActionClass, hookList.toPersistentList().add(hook))
    }

    internal fun build(): WaypointActionMap = WaypointActionMap(
        resolvers = actionResolvers.toImmutableMap(),
        hooks = hooks.toImmutableMap(),
    )
}

inline fun <reified T: WaypointAction> WaypointActionMapBuilder.onAction(
    noinline waypointActionResolverBuilder: () -> WaypointActionResolver,
) = onAction(T::class, waypointActionResolverBuilder())

inline fun <reified T: WaypointAction> WaypointActionMapBuilder.onAction(
    noinline block: (waypointHolder: MutableWaypointHolder, waypointAction: T) -> Unit,
) = onAction(T::class, waypointActionResolver<T>(block = block))

inline fun <reified T: WaypointAction> WaypointActionMapBuilder.addHook(
    hook: WaypointActionHook
) = addHook(waypointActionClass = T::class, hook = hook)

inline fun <reified T: WaypointAction> WaypointActionMapBuilder.addHook(
    crossinline preResolveHook: (waypointHolder: WaypointHolder, waypointAction: WaypointAction) -> Unit,
    crossinline postResolveHook: (waypointHolder: WaypointHolder, waypointAction: WaypointAction) -> Unit,
) = addHook(
    waypointActionClass = T::class,
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

fun buildWaypointActions(
    builder: WaypointActionMapBuilder.() -> Unit,
): WaypointActionMap {
    return WaypointActionMapBuilder().apply(builder).build()
}
