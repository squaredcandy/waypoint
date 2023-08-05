package com.squaredcandy.waypoint.core.semantics

import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import com.squaredcandy.waypoint.core.Identifier
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.action.WaypointAction
import com.squaredcandy.waypoint.core.route.WaypointRoute
import com.squaredcandy.waypoint.core.semantics.SemanticsProperties.WaypointHolderSemanticsKey

//region Waypoint Holder
fun SemanticsNodeInteraction.assertWaypointListEqualTo(
    waypointList: List<Waypoint>?,
): SemanticsNodeInteraction = assert(hasWaypointListExactly(waypointList))

fun SemanticsNodeInteraction.assertWaypointListInstanceEqualAfter(
    action: SemanticsNodeInteraction.() -> SemanticsNodeInteraction,
): SemanticsNodeInteraction {
    val initialInstance = getSemanticsProperty(WaypointHolderSemanticsKey)
        ?.waypointList
    return action().assert(isWaypointListInstanceExactly(initialInstance))
}

fun SemanticsNodeInteraction.assertWaypointListInstanceNotEqualAfter(
    action: SemanticsNodeInteraction.() -> SemanticsNodeInteraction,
): SemanticsNodeInteraction {
    val initialInstance = getSemanticsProperty(WaypointHolderSemanticsKey)
        ?.waypointList
    return action().assert(isWaypointListInstanceNotExactly(initialInstance))
}

fun SemanticsNodeInteractionCollection.assertAllWaypointListEqualTo(
    waypointList: List<Waypoint>?,
): SemanticsNodeInteractionCollection = assertAll(hasWaypointListExactly(waypointList))

fun SemanticsNodeInteractionCollection.assertCorrectWaypointHolderParentTree(): SemanticsNodeInteractionCollection {
    val nodes = fetchSemanticsNodes()
    assert(nodes.size > 1) { "Only one Waypoint Holder Node found" }
    nodes.indices
        .map(this::get)
        .zipWithNext()
        .forEach { (a, b) ->
            a.assert(hasWaypointHolderParentExactly(b.getSemanticsProperty(WaypointHolderSemanticsKey)?.parent))
        }
    return this
}

fun SemanticsNodeInteractionCollection.assertWaypointListEqualTo(
    index: Int,
    waypointList: List<Waypoint>?,
): SemanticsNodeInteractionCollection {
    get(index).assert(hasWaypointListExactly(waypointList))
    return this
}
//endregion

//region Waypoint Action
inline fun <reified T : WaypointAction> SemanticsNodeInteraction.assertWaypointActionExists(): SemanticsNodeInteraction =
    assert(hasWaypointActionExactly(T::class))

inline fun <reified T : WaypointAction> SemanticsNodeInteraction.assertWaypointActionDoesNotExist(): SemanticsNodeInteraction =
    assert(hasWaypointActionNotExactly(T::class))
//endregion

//region Waypoint Route
fun <T : WaypointRoute<T>> SemanticsNodeInteraction.assertWaypointRouteExists(
    routeKey: Identifier<T>,
): SemanticsNodeInteraction = assert(hasWaypointRouteKey(routeKey))

fun <T: WaypointRoute<T>> SemanticsNodeInteraction.assertWaypointRouteDoesNotExist(
    routeKey: Identifier<T>,
): SemanticsNodeInteraction = assert(hasNotWaypointRouteKey(routeKey))

fun <T: WaypointRoute<T>> SemanticsNodeInteraction.assertWaypointRouteEqualTo(
    routeKey: Identifier<T>,
    routeWaypointList: T.() -> List<Waypoint>,
    waypointList: List<Waypoint>?,
): SemanticsNodeInteraction = assert(hasWaypointRouteExactly(routeKey, routeWaypointList, waypointList))
//endregion
