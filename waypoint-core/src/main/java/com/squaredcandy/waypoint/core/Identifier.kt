package com.squaredcandy.waypoint.core

import java.io.Serializable
import java.util.UUID

@Suppress("unused")
@JvmInline
value class Identifier<T>(val id: String): Serializable by id

fun <T> randomIdentifier() = Identifier<T>(UUID.randomUUID().toString())
