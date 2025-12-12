package dev.echoellet.dragonfist_legacy.entity.shifu.handlers

import dev.echoellet.dragonfist_legacy.entity.bandit.handler.NearbyPlayersMessenger
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageKeys
import dev.echoellet.dragonfist_legacy.util.minecraftRandom
import dev.echoellet.dragonfist_legacy.util.randomChance
import dev.echoellet.dragonfist_legacy.util.secondsToTicks

class ShifuRainReactionManager(
    private val entity: ShifuEntity
) {
    companion object {
        private object MessageDelayTicks {
            val MIN = 30.secondsToTicks
            val MAX = 60.secondsToTicks

            val MIN_NEXT_ATTEMPT = 10.secondsToTicks
        }

        private const val CHANCE_TO_SPEAK = 0.50 // 50% chance to speak
    }

    private var cooldown = MessageDelayTicks.MIN

    /** Called every tick to potentially reacts to the Rain if it's raining. Must be called **only** on the server side. */
    fun tick() {
        val level = entity.level()

        if (!level.isRaining) return

        if (cooldown > 0) {
            cooldown--
            return
        }

        if (entity.isInCombat()) return

        val shouldSpeak = entity.random.randomChance(CHANCE_TO_SPEAK)
        if (!shouldSpeak) {
            cooldown = MessageDelayTicks.MIN_NEXT_ATTEMPT
            return
        }

        if (!entity.isAlive) return

        sendMessageToNearbyPlayers()

        cooldown = (MessageDelayTicks.MIN..MessageDelayTicks.MAX).minecraftRandom(entity.random)
    }

    private fun sendMessageToNearbyPlayers() {
        val messageKey = ShifuMessageKeys.RANDOM_RAIN_REACTION.minecraftRandom(entity.random)
        NearbyPlayersMessenger.sendMessage(messageKey, entity)
    }
}
