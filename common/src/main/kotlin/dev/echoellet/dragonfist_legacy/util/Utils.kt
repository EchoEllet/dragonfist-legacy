package dev.echoellet.dragonfist_legacy.util

import dev.echoellet.dragonfist_legacy.util.constants.Constants

val Int.secondsToTicks: Int
    get() = this * Constants.TICKS_PER_SECOND
