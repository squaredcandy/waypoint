package com.squaredcandy.waypoint.core.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.modifier.modifierLocalConsumer

// TODO Maybe wrap ModifierLocalReadScope inside our own scope to handle nullable better
fun Modifier.waypointContent(
    content: @Composable ModifierLocalReadScope.() -> Unit,
): Modifier = composed {
    var modifierLocalReadScope by remember {
        mutableStateOf<ModifierLocalReadScope?>(null)
    }
    modifierLocalReadScope?.let { scope ->
        with(scope) {
            content()
        }
    }

    modifierLocalConsumer {
        modifierLocalReadScope = this
    }
}
