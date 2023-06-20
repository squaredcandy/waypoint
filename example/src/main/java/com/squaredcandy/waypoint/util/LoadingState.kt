package com.squaredcandy.waypoint.util

sealed interface LoadingState<out T> {
    data class Loaded<T>(val item: T): LoadingState<T>
    object Loading : LoadingState<Nothing>
}
