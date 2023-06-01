package com.squaredcandy.waypoint.core.action

import androidx.compose.ui.modifier.modifierLocalOf
import kotlin.reflect.KClass

interface WaypointActionProvider {
    fun <T: WaypointAction> getAction(waypointActionClass: KClass<T>): WaypointActionResolver<T>?
}

inline fun <reified T: WaypointAction> WaypointActionProvider.getAction(): WaypointActionResolver<T>? = getAction(T::class)

val noOpWaypointActionProvider = object : WaypointActionProvider {
    override fun <T : WaypointAction> getAction(waypointActionClass: KClass<T>): WaypointActionResolver<T>? = null
}

val ModifierLocalWaypointActionProvider = modifierLocalOf { noOpWaypointActionProvider }
