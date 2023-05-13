package com.squaredcandy.waypoint.core.action

import androidx.compose.ui.modifier.ModifierLocalReadScope
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

    context(ModifierLocalReadScope)
    fun <T: WaypointAction> addAction(waypointActionClass: KClass<T>, waypointActionResolver: WaypointActionResolver) {
        actionResolvers = actionResolvers.put(waypointActionClass, waypointActionResolver)
    }

    context(ModifierLocalReadScope)
    fun <T: WaypointAction> addHook(waypointActionClass: KClass<T>, hook: WaypointActionHook) {
        val hookList = hooks.getOrDefault(waypointActionClass, persistentListOf())
        hooks = hooks.put(waypointActionClass, hookList.toPersistentList().add(hook))
    }

    internal fun build(): WaypointActionMap = WaypointActionMap(
        resolvers = actionResolvers.toImmutableMap(),
        hooks = hooks.toImmutableMap(),
    )
}

context(ModifierLocalReadScope, WaypointActionMapBuilder)
inline fun <reified T: WaypointAction> addAction(
    noinline waypointActionResolverBuilder: () -> WaypointActionResolver,
) = addAction(T::class, waypointActionResolverBuilder())

context(ModifierLocalReadScope, WaypointActionMapBuilder)
inline fun <reified T: WaypointAction> addAction(
    noinline block: (waypointHolder: MutableWaypointHolder, waypointAction: T) -> Unit,
) = addAction(T::class, waypointActionResolver<T>(block = block))

context(ModifierLocalReadScope, WaypointActionMapBuilder)
inline fun <reified T: WaypointAction> addHook(
    hook: WaypointActionHook
) = addHook(waypointActionClass = T::class, hook = hook)

context(ModifierLocalReadScope, WaypointActionMapBuilder)
inline fun <reified T: WaypointAction> addHook(
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

fun ModifierLocalReadScope.buildWaypointActions(
    builder: context(ModifierLocalReadScope) WaypointActionMapBuilder.() -> Unit,
): WaypointActionMap {
    return WaypointActionMapBuilder().apply { builder(this@buildWaypointActions, this) }.build()
}
