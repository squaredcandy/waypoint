package com.squaredcandy.waypoint.core.action

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlin.reflect.KClass

data class WaypointActionMap(
    val resolvers: ImmutableMap<KClass<*>, WaypointActionResolver>,
    val hooks: ImmutableMap<KClass<*>, ImmutableList<WaypointActionHook>>,
)
