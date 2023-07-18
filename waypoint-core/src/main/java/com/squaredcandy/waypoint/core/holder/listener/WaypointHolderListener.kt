package com.squaredcandy.waypoint.core.holder.listener

@ExperimentalWaypointHolderListenerModifier
fun interface WaypointHolderListener {
    fun onWaypointHolderChanged(changeList: WaypointHolderChangeData)
}
