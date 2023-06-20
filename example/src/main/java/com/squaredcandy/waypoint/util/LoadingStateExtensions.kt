package com.squaredcandy.waypoint.util

fun <T> LoadingState<T>.getLoadedOrNull(): T? = (this as? LoadingState.Loaded<T>)?.item

fun <T> LoadingState<T>.isLoaded(): Boolean = this !is LoadingState.Loading

fun <T, S> LoadingState<T>.mapTo(transform: (T) -> S): LoadingState<S> = when (this) {
    is LoadingState.Loaded -> LoadingState.Loaded(transform(this.item))
    is LoadingState.Loading -> LoadingState.Loading
}
