package com.squaredcandy.waypoint.core.route

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class WaypointTransitionSpecTypeTest(
    private val isNavigate: Boolean,
    private val isEnter: Boolean,
    private val result: WaypointTransitionSpecType,
) {

    @Test
    fun `TEST all WaypointTransitionSpecType combinations`() = runTest {
        assertThat(WaypointTransitionSpecType.of(isNavigate, isEnter)).isEqualTo(result)
    }

    companion object {
        @Parameters
        @JvmStatic
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(true, true, WaypointTransitionSpecType.NavigateEnter),
                arrayOf(true, false, WaypointTransitionSpecType.NavigateExit),
                arrayOf(false, true, WaypointTransitionSpecType.BacktrackEnter),
                arrayOf(false, false, WaypointTransitionSpecType.BacktrackExit),
            )
        }
    }
}
