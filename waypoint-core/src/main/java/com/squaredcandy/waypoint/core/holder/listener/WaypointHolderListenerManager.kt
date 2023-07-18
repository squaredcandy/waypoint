package com.squaredcandy.waypoint.core.holder.listener

@ExperimentalWaypointHolderListenerModifier
interface WaypointHolderListenerManager {
    fun registerOnWaypointHolderChanged(listener: WaypointHolderListener)
    fun deregisterOnWaypointHolderChanged(listener: WaypointHolderListener)
}
