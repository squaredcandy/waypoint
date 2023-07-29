package com.squaredcandy.waypoint.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.zip

data class WaypointRouteChangeList(
    val active: List<Waypoint>,
    val inactive: List<Waypoint>,
    val removed: List<Waypoint>,
)

fun Flow<List<Waypoint>>.toWaypointRouteChangeList(waypointHolderListFlow: Flow<List<Waypoint>>): Flow<WaypointRouteChangeList> =
    this.runningFold<List<Waypoint>, Pair<List<Waypoint>, List<Waypoint>>>(
        emptyList<Waypoint>() to emptyList(),
    ) { last, current -> last.second to current }
        .filter { (oldList, newList) -> oldList.isNotEmpty() || newList.isNotEmpty() }
        .zip(waypointHolderListFlow) { (oldList, newList), waypointHolderList ->
            val activeList = newList.minus(oldList.toHashSet())
            val notInActiveList = oldList.minus(newList.toHashSet())
            val (inactiveList, removedList) = notInActiveList.partition {
                it in waypointHolderList
            }

            WaypointRouteChangeList(
                active = activeList,
                inactive = inactiveList,
                removed = removedList,
            )
        }
        .filter { (active, inactive, removed) ->
            active.isNotEmpty() || inactive.isNotEmpty() || removed.isNotEmpty()
        }
