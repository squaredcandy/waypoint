package com.squaredcandy.waypoint.core.holder

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import com.squaredcandy.waypoint.core.Waypoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal class DefaultWaypointHolder(
    private val mutableWaypointList: MutableList<Waypoint>,
    override val parent: MutableWaypointHolder? = null,
) : MutableWaypointHolder {
    override val waypointList: ImmutableList<Waypoint> by derivedStateOf { mutableWaypointList.toImmutableList() }

    override val isDefined: Boolean = true

    override var lastNavigationType: WaypointNavigationType? = null

    override fun <T> updateWaypointList(
        navigationType: WaypointNavigationType,
        update: MutableList<Waypoint>.() -> T
    ): T {
        lastNavigationType = navigationType
        return mutableWaypointList.let(update)
    }
}
