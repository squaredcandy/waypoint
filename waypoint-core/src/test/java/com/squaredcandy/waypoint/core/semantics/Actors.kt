package com.squaredcandy.waypoint.core.semantics

import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.test.SemanticsNodeInteraction
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.handle.WaypointHandle
import com.squaredcandy.waypoint.core.holder.WaypointNavigationType
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointActionProviderSemanticsKey
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointHandleProviderSemanticKey
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointHolderSemanticsKey
import kotlin.reflect.KClass

//region Waypoint Holder
fun <T> SemanticsNodeInteraction.updateWaypointList(
    navigationType: WaypointNavigationType,
    update: MutableList<Waypoint>.() -> T,
): SemanticsNodeInteraction {
    getSemanticsProperty(WaypointHolderSemanticsKey)
        ?.updateWaypointList(navigationType, update)
    return this
}
//endregion

//region Waypoint Holder
inline fun <reified T: WaypointAction> SemanticsNodeInteraction.invokeWaypointAction(
    action: T,
): SemanticsNodeInteraction = invokeWaypointAction(T::class, action)

fun <T: WaypointAction> SemanticsNodeInteraction.invokeWaypointAction(
    clazz: KClass<T>,
    action: T,
): SemanticsNodeInteraction {
    val waypointHolder = getSemanticsProperty(WaypointHolderSemanticsKey)
    getSemanticsProperty(WaypointActionProviderSemanticsKey)
        ?.getAction(clazz)
        ?.invoke(waypointHolder!!, action)

    return this
}

inline fun <reified T: WaypointAction> SemanticsNodeInteraction.invokeWaypointActionOnNode(
    noinline node: () -> SemanticsNodeInteraction,
    action: T,
): SemanticsNodeInteraction = invokeWaypointActionOnNode(T::class, node, action)

fun <T: WaypointAction> SemanticsNodeInteraction.invokeWaypointActionOnNode(
    clazz: KClass<T>,
    node: () -> SemanticsNodeInteraction,
    action: T,
): SemanticsNodeInteraction {
    val waypointHolder = node().getSemanticsProperty(WaypointHolderSemanticsKey)
    getSemanticsProperty(WaypointActionProviderSemanticsKey)
        ?.getAction(clazz)
        ?.invoke(waypointHolder!!, action)

    return this
}
//endregion

//region Waypoint Handle
fun <T: WaypointHandle> SemanticsNodeInteraction.invokeWithWaypointHandle(
    waypoint: Waypoint,
    constructor: (Identifier<Waypoint>, ModifierLocalReadScope) -> T,
    withWaypointHandle: (T) -> Unit,
): SemanticsNodeInteraction {
    val waypointHandleProvider = getSemanticsProperty(WaypointHandleProviderSemanticKey)
    waypointHandleProvider!!.buildWaypointHandle(constructor, waypoint)
        .apply(withWaypointHandle)
    return this
}
//endregion
