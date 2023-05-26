package com.squaredcandy.waypoint.core.holder

enum class WaypointTransitionSpecType(val isNavigate: Boolean, val isEnter: Boolean) {
    NavigateEnter(isNavigate = true, isEnter = true),
    NavigateExit(isNavigate = true, isEnter = false),
    BacktrackEnter(isNavigate = false, isEnter = true),
    BacktrackExit(isNavigate = false, isEnter = false),
    ;

    companion object {
        fun of(isNavigate: Boolean, isEnter: Boolean): WaypointTransitionSpecType = when {
            isNavigate && isEnter -> NavigateEnter
            isNavigate && !isEnter -> NavigateExit
            !isNavigate && isEnter -> BacktrackEnter
            else -> BacktrackExit
        }
    }
}
