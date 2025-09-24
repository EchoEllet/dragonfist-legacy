package dev.echoellet.dragonfist_legacy.entity.shifu.combat

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageKeys
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageUtils
import net.minecraft.util.RandomSource

class ShifuPlayerTrainingIntroManager {
    companion object {
        private fun getRandomTrainingStartsMessageKey(randomSource: RandomSource, isRaining: Boolean) =
            ShifuMessageUtils.getRandomMessageKey(
                randomSource,
                ShifuMessageKeys.PlayerTraining.Start.NORMAL,
                ShifuMessageKeys.PlayerTraining.Start.RAIN,
                isRaining
            )
    }

    private val activeTrainingPlayers = mutableSetOf<String>()

    /**
     * Called when a player damages Shifu.
     * Triggers the training start message if this is the first hit in the training session.
     */
    fun notifyPlayerHit(player: Player) {
        if (isInTraining(player)) return
        startTrainingFor(player)
    }

    /**
     * Called when Shifu's target changes.
     * Resets training state for the previous target if the training session ended.
     */
    fun handleTargetChange(previousTarget: Player, newTarget: Entity?) {
        if (newTarget == previousTarget) return
        endTrainingFor(previousTarget)
    }

    private fun startTrainingFor(player: Player) {
        activeTrainingPlayers.add(player.stringUUID)

        val messageKey = getRandomTrainingStartsMessageKey(player.random, player.level().isRaining)
        player.displayClientMessage(
            Component.translatable(messageKey),
            false,
        )
    }

    private fun endTrainingFor(player: Player) {
        activeTrainingPlayers.remove(player.stringUUID)
    }

    /**
     * Returns whether the given player is currently undergoing training with Shifu.
     *
     * Important distinction:
     * - [ShifuEntity.isInCombat] only checks if Shifu has a target (any target),
     *   meaning Shifu is engaged in combat in general. It does not track which player(s) are training.
     * - This method tracks training **per player**, using [activeTrainingPlayers] set,
     *   so it correctly handles multiple players training with Shifu independently.
     *   It ensures messages like "Training starts!" are only triggered once per player per training session.
     */
    private fun isInTraining(player: Player) = player.stringUUID in activeTrainingPlayers
}
