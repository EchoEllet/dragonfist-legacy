package dev.echoellet.dragonfist_legacy.entity.shifu.util

import dev.echoellet.dragonfist_legacy.util.minecraftRandom
import net.minecraft.util.RandomSource

object ShifuMessageUtils {
    fun getRandomMessageKey(
        random: RandomSource,
        normalKeys: List<String>,
        rainKeys: List<String>,
        isRaining: Boolean
    ): String {
        val messageKeys = normalKeys + if (isRaining) rainKeys else emptyList()
        return messageKeys.minecraftRandom(random)
    }
}
