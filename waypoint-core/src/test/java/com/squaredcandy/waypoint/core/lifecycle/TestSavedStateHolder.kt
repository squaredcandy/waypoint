package com.squaredcandy.waypoint.core.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.SaveableStateHolder

class TestSavedStateHolder(
    private val onRemoveState: (key: Any) -> Unit
) : SaveableStateHolder {
    @Composable
    override fun SaveableStateProvider(key: Any, content: @Composable () -> Unit) { content() }

    override fun removeState(key: Any) { onRemoveState(key) }
}
