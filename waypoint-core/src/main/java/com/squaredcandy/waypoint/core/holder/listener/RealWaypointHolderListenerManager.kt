package com.squaredcandy.waypoint.core.holder.listener

import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.ModifierLocalModifierNode
import androidx.compose.ui.modifier.modifierLocalOf
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.holder.ModifierLocalWaypointHolder
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
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
        MutableSharedFlow<WaypointHolderChangeData>(extraBufferCapacity = 64)

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
            snapshotFlow { waypointHolder.waypointList }
                .runningFold<ImmutableList<Waypoint>, Pair<ImmutableList<Waypoint>, ImmutableList<Waypoint>>>(
                    persistentListOf<Waypoint>() to persistentListOf(),
                ) { last, current -> last.second to current }
                .drop(1) // Drop first iteration
                .map { (old, new) ->
                    val addedItemList = new.minus(old.toHashSet())
                    val removedItemList = old.minus(new.toHashSet())
                    mutableSharedFlow.emit(
                        WaypointHolderChangeData(
                            added = addedItemList,
                            removed = removedItemList,
                        )
                    )
                }
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
