package com.squaredcandy.waypoint.core.action

import kotlinx.collections.immutable.ImmutableMap
import kotlin.reflect.KClass

internal data class WaypointActionSet(
    val resolvers: ImmutableMap<KClass<*>, WaypointActionResolver<*>>,
)
