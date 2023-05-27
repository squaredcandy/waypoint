package com.squaredcandy.waypoint.core.route

enum class WaypointTransitionSpecType {
    NavigateEnter,
    NavigateExit,
    BacktrackEnter,
    BacktrackExit,
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
