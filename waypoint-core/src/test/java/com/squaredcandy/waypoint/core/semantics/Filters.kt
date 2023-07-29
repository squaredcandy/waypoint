package com.squaredcandy.waypoint.core.semantics

import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.test.SemanticsMatcher
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.handle.WaypointHandle
import com.squaredcandy.waypoint.core.holder.WaypointHolder
import com.squaredcandy.waypoint.core.route.WaypointRoute
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointActionProviderSemanticsKey
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointHandleProviderSemanticKey
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointHolderSemanticsKey
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointRouteProviderSemanticsKey
import kotlinx.collections.immutable.ImmutableList
import kotlin.reflect.KClass

//region Waypoint Holder
fun hasWaypointHolder() = SemanticsMatcher.keyIsDefined(WaypointHolderSemanticsKey)

fun hasWaypointListExactly(
    waypointList: List<Waypoint>?,
) = SemanticsMatcher("Waypoint list has exactly $waypointList") { semanticsNode ->
    val currentWaypointHolder = semanticsNode
        .getSemanticsProperty(WaypointHolderSemanticsKey)
    currentWaypointHolder?.waypointList == waypointList
}

fun isWaypointListInstanceExactly(
    instance: ImmutableList<Waypoint>?
) = SemanticsMatcher("Waypoint list instance is $instance") { semanticsNode ->
    val currentInstance = semanticsNode
        .getSemanticsProperty(WaypointHolderSemanticsKey)
        ?.waypointList
    currentInstance === instance
}

fun isWaypointListInstanceNotExactly(
    instance: ImmutableList<Waypoint>?
) = SemanticsMatcher("Waypoint list instance is not $instance") { semanticsNode ->
    val currentInstance = semanticsNode
        .getSemanticsProperty(WaypointHolderSemanticsKey)
        ?.waypointList
    currentInstance !== instance
}

fun hasWaypointHolderParentExactly(
    parent: WaypointHolder?
) = SemanticsMatcher("Waypoint Holder parent is $parent") { semanticsNode ->
    val currentParent = semanticsNode
        .getSemanticsProperty(WaypointHolderSemanticsKey)
        ?.parent
    currentParent !== parent
}
//endregion

//region Waypoint Action
fun hasWaypointActionProvider() = SemanticsMatcher.keyIsDefined(WaypointActionProviderSemanticsKey)

fun <T: WaypointAction> hasWaypointActionExactly(clazz: KClass<T>) = SemanticsMatcher(
    "Has ${clazz.simpleName}",
) { semanticsNode ->
    val waypointActionProvider = semanticsNode
        .getSemanticsProperty(WaypointActionProviderSemanticsKey)
    waypointActionProvider?.getAction(clazz) != null
}

fun <T: WaypointAction> hasWaypointActionNotExactly(clazz: KClass<T>) = SemanticsMatcher(
    "Has ${clazz.simpleName}",
) { semanticsNode ->
    val waypointActionProvider = semanticsNode
        .getSemanticsProperty(WaypointActionProviderSemanticsKey)
    waypointActionProvider?.getAction(clazz) == null
}
//endregion

//region Waypoint Route
fun hasWaypointRoute() = SemanticsMatcher.keyIsDefined(WaypointRouteProviderSemanticsKey)

fun <T: WaypointRoute<T>> hasWaypointRouteKey(
    routeKey: Identifier<T>,
) = SemanticsMatcher("Has $routeKey") { semanticsNode ->
    val waypointRouteProvider = semanticsNode
        .getSemanticsProperty(WaypointRouteProviderSemanticsKey)
    waypointRouteProvider?.getRoute(routeKey) != null
}

fun <T: WaypointRoute<T>> hasNotWaypointRouteKey(
    routeKey: Identifier<T>,
) = SemanticsMatcher("Has $routeKey") { semanticsNode ->
    val waypointRouteProvider = semanticsNode
        .getSemanticsProperty(WaypointRouteProviderSemanticsKey)
    runCatching { waypointRouteProvider?.getRoute(routeKey) }.isFailure
}

fun <T: WaypointRoute<T>> hasWaypointRouteExactly(
    routeKey: Identifier<T>,
    routeWaypointList: T.() -> List<Waypoint>,
    waypointList: List<Waypoint>?,
) = SemanticsMatcher("$routeKey is exactly $waypointList") { semanticsNode ->
    val waypointRouteProvider = semanticsNode
        .getSemanticsProperty(WaypointRouteProviderSemanticsKey)
    waypointRouteProvider?.getRoute(routeKey)?.routeWaypointList() == waypointList
}
//endregion

//region Waypoint Handle
fun hasWaypointHandleProvider() = SemanticsMatcher.keyIsDefined(WaypointHandleProviderSemanticKey)

fun <T: WaypointHandle> hasWaypointHandleExactly(
    clazz: KClass<T>,
    waypoint: Waypoint,
    constructor: (Identifier<Waypoint>, ModifierLocalReadScope) -> T,
) = SemanticsMatcher("Has $clazz") { semanticsNode ->
    val waypointHandleProvider = semanticsNode.getSemanticsProperty(WaypointHandleProviderSemanticKey)
    waypointHandleProvider?.buildWaypointHandle(constructor, waypoint) != null
}

fun <T: WaypointHandle> hasNotWaypointHandle(
    clazz: KClass<T>,
    waypoint: Waypoint,
    constructor: (Identifier<Waypoint>, ModifierLocalReadScope) -> T,
) = SemanticsMatcher("Has $clazz") { semanticsNode ->
    val waypointHandleProvider = semanticsNode.getSemanticsProperty(WaypointHandleProviderSemanticKey)
    runCatching { waypointHandleProvider?.buildWaypointHandle(constructor, waypoint) }.isFailure
}
//endregion
