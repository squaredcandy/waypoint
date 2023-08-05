package com.squaredcandy.waypoint.core.action

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.modifier.ModifierLocalReadScope
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import kotlin.reflect.KClass

val LocalWaypointActionSender = compositionLocalOf<WaypointActionSender> { noOpWaypointActionSender }

interface WaypointActionSender {
    fun <T: WaypointAction> sendAction(waypointActionClass: KClass<T>, waypointAction: T): Result<Unit>
}

@Composable
fun ModifierLocalReadScope.createWaypointActionSender(): WaypointActionSender {
    val mutableWaypointHolder = ModifierLocalMutableWaypointHolder.current
    val waypointActionProvider = ModifierLocalWaypointActionProvider.current
    return remember(mutableWaypointHolder, waypointActionProvider) {
        object : WaypointActionSender {
            override fun <T : WaypointAction> sendAction(
                waypointActionClass: KClass<T>,
                waypointAction: T,
            ): Result<Unit> {
                return runCatching {
                    waypointActionProvider.getAction(waypointActionClass)!!
                        .invoke(mutableWaypointHolder, waypointAction)
                }
            }
        }
    }
}

private val noOpWaypointActionSender = object : WaypointActionSender {
    override fun <T : WaypointAction> sendAction(
        waypointActionClass: KClass<T>,
        waypointAction: T
    ): Result<Unit> = Result.failure(NotImplementedError("sendAction not implemented"))
}

inline fun <reified T: WaypointAction> WaypointActionSender.sendAction(waypointAction: T): Result<Unit> {
    return sendAction(T::class, waypointAction)
}

inline operator fun <reified T: WaypointAction> WaypointActionSender.invoke(waypointAction: T): Result<Unit> {
    return sendAction(T::class, waypointAction)
}
