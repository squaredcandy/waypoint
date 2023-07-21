package com.squaredcandy.waypoint.core.holder.listener

import com.squaredcandy.waypoint.core.WaypointChangeList

@ExperimentalWaypointHolderListenerModifier
fun interface WaypointHolderListener {
    fun onWaypointHolderChanged(changeList: WaypointChangeList)
}
