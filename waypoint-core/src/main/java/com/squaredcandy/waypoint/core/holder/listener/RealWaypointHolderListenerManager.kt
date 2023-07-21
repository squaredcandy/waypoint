package com.squaredcandy.waypoint.core.holder.listener

import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.WaypointChangeList
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import com.squaredcandy.waypoint.core.toWaypointChangeListFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@ExperimentalWaypointHolderListenerModifier
val ModifierLocalWaypointHolderListenerManager =
    modifierLocalOf<WaypointHolderListenerManager> { error("Missing WaypointHolderListenerManager") }

@ExperimentalWaypointHolderListenerModifier
internal class RealWaypointHolderListenerManager :
    WaypointHolderListenerManager,
    ModifierLocalModifierNode,
    Modifier.Node() {

    private val mutableSharedFlow =
        MutableSharedFlow<WaypointChangeList>(extraBufferCapacity = 64)

    private val listeners = mutableListOf<WaypointHolderListener>()

    override fun registerOnWaypointHolderChanged(listener: WaypointHolderListener) {
        listeners.add(listener)
    }

    override fun deregisterOnWaypointHolderChanged(listener: WaypointHolderListener) {
        listeners.remove(listener)
    }

    override fun onAttach() {
        super.onAttach()
        val waypointHolder = ModifierLocalWaypointHolder.current
        coroutineScope.launch {
            waypointHolder.waypointList.toWaypointChangeListFlow()
                .onEach(mutableSharedFlow::emit)
                .launchIn(this)

            enableListener()
        }
    }

    private suspend fun enableListener() {
        mutableSharedFlow.collect { changeList ->
            listeners.forEach { listener ->
                listener.onWaypointHolderChanged(changeList)
            }
        }
    }
}
