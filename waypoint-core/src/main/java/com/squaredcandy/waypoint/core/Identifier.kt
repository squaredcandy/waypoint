package com.squaredcandy.waypoint.core

import java.util.UUID

@Suppress("unused")
@JvmInline
value class Identifier<T>(val id: String)

fun <T> randomIdentifier() = Identifier<T>(UUID.randomUUID().toString())
