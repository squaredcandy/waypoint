package com.squaredcandy.waypoint.core.action

import androidx.compose.ui.modifier.modifierLocalOf
import kotlin.reflect.KClass

fun interface WaypointActionProvider {
    fun getAction(waypointActionClass: KClass<*>): WaypointActionResolver?
}

inline fun <reified T: WaypointAction> WaypointActionProvider.getAction(): WaypointActionResolver? = getAction(T::class)

private val noOpWaypointActionProvider = WaypointActionProvider { null }

val ModifierLocalWaypointActionProvider = modifierLocalOf { noOpWaypointActionProvider }
