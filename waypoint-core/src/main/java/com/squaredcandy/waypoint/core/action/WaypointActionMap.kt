package com.squaredcandy.waypoint.core.action

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlin.reflect.KClass

internal data class WaypointActionMap(
    val resolvers: ImmutableMap<KClass<*>, WaypointActionResolver<*>>,
    val hooks: ImmutableList<WaypointActionHook>,
)
