package dev.echoellet.dragonfist_legacy.util

import net.minecraft.Util
import net.minecraft.util.RandomSource
import kotlin.enums.EnumEntries

fun <T : Enum<T>> EnumEntries<T>.minecraftRandom(randomSource: RandomSource): T {
    val values = this
    return Util.getRandom(values, randomSource)
}

fun <T> List<T>.minecraftRandom(randomSource: RandomSource): T {
    val selections = this
    return Util.getRandom(selections, randomSource)
}

fun IntRange.minecraftRandom(randomSource: RandomSource): Int {
    return randomSource.nextInt(this.first, this.last + 1)
}

/**
 * Generates a random chance based on a given probability in the range 0.0 to 1.0.
 *
 * @param probability The chance (0.0-1.0) of the event occurring, represented as a double.
 * @return Whether the event should occur based on the provided probability.
 */
fun RandomSource.randomChance(probability: Double): Boolean {
    require(probability in 0.0..1.0) { "Probability must be between 0.0 and 1.0" }

    val randomNumber = this.nextDouble()

    return randomNumber < probability
}

fun RandomSource.getRandomFocusSeconds(minSeconds: Int, maxSeconds: Int): Int {
    return (minSeconds..maxSeconds).minecraftRandom(this)
}
