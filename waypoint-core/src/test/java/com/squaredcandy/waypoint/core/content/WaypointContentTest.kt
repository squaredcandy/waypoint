package com.squaredcandy.waypoint.core.content

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.squaredcandy.waypoint.core.Waypoint
import com.squaredcandy.waypoint.core.holder.ModifierLocalMutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.MutableWaypointHolder
import com.squaredcandy.waypoint.core.holder.waypointHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaypointContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `GIVEN we have a waypoint holder WHEN we retrieve it in waypoint content THEN waypoint holder is not null`() {
        val list = listOf(Waypoint())

        var waypointHolder: State<MutableWaypointHolder?> = mutableStateOf(null)
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointHolder(list)
                    .waypointContent {
                        waypointHolder = rememberModifierLocalState(modifierLocal = ModifierLocalMutableWaypointHolder)
                    }
            )
        }

        Truth.assertThat(waypointHolder.value).isNotNull()
    }

    @Test
    fun `GIVEN we have don't have a waypoint holder WHEN we retrieve it in waypoint content THEN waypoint holder is null`() {
        var waypointHolder: State<MutableWaypointHolder?> = mutableStateOf(null)
        composeTestRule.setContent {
            Box(
                modifier = Modifier
                    .waypointContent {
                        waypointHolder = rememberModifierLocalState(modifierLocal = ModifierLocalMutableWaypointHolder)
                    }
            )
        }

        Truth.assertThat(waypointHolder.value).isNull()
    }
}
