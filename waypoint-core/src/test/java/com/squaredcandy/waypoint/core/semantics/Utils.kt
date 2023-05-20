package com.squaredcandy.waypoint.core.semantics

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction

fun <T> SemanticsNodeInteraction.getSemanticsProperty(
    semanticsPropertyKey: SemanticsPropertyKey<T>,
): T? = fetchSemanticsNode().config.getOrNull(semanticsPropertyKey)

fun <T> SemanticsNode.getSemanticsProperty(
    semanticsPropertyKey: SemanticsPropertyKey<T>,
): T? = config.getOrNull(semanticsPropertyKey)
