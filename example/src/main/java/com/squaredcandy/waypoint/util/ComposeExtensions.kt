package com.squaredcandy.waypoint.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Collect a flow, wrapping it in a [LoadingState] so we can handled loading states.
 */
@Composable
fun <T> Flow<T>.collectAsLoadingState(): State<LoadingState<T>> {
    return produceState<LoadingState<T>>(initialValue = LoadingState.Loading) {
        this@collectAsLoadingState.collect { value = LoadingState.Loaded(it) }
    }
}

/**
 * Collect a flow, wrapping it in a [LoadingState] so we can handled loading states.
 * Also allows us to also transform [T] into a more suitable type.
 */
@Composable
fun <T, S> Flow<T>.collectAsMappedLoadingState(transform: suspend (T) -> S?): State<LoadingState<S>> {
    return produceState<LoadingState<S>>(initialValue = LoadingState.Loading) {
        this@collectAsMappedLoadingState
            .mapNotNull(transform)
            .collect { value = LoadingState.Loaded(it) }
    }
}

/**
 * Remembering a function allows us compose to skip recomposition unless there is a change to [key1]
 */
@Composable
fun rememberFunc(key1: Any? = null, method: () -> Unit): () -> Unit = remember(key1 = key1) { method }
