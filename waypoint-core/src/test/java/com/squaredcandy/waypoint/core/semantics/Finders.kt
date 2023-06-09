package com.squaredcandy.waypoint.core.semantics

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider

//region Waypoint Holder
fun SemanticsNodeInteractionsProvider.onWaypointHolderNode(
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteraction {
    return onNode(
        matcher = hasWaypointHolder(),
        useUnmergedTree = useUnmergedTree,
    )
}

fun SemanticsNodeInteractionsProvider.onWaypointHolderNodes(
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteractionCollection {
    return onAllNodes(
        matcher = hasWaypointHolder(),
        useUnmergedTree = useUnmergedTree,
    )
}
//endregion

//region Waypoint Action
fun SemanticsNodeInteractionsProvider.onWaypointActionProviderNode(
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteraction {
    return onNode(
        matcher = hasWaypointActionProvider(),
        useUnmergedTree = useUnmergedTree,
    )
}

fun SemanticsNodeInteractionsProvider.onWaypointActionProviderNodes(
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteractionCollection {
    return onAllNodes(
        matcher = hasWaypointActionProvider(),
        useUnmergedTree = useUnmergedTree,
    )
}
//endregion

//region Waypoint Route
fun SemanticsNodeInteractionsProvider.onWaypointRouteNode(
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteraction {
    return onNode(
        matcher = hasWaypointRoute(),
        useUnmergedTree = useUnmergedTree,
    )
}
//endregion

//region Waypoint Lifecycle
fun SemanticsNodeInteractionsProvider.onWaypointLifecycleOwnerNode(
    useUnmergedTree: Boolean = false,
): SemanticsNodeInteraction {
    return onNode(
        matcher = hasWaypointLifecycleOwner(),
        useUnmergedTree = useUnmergedTree,
    )
}
//endregion
