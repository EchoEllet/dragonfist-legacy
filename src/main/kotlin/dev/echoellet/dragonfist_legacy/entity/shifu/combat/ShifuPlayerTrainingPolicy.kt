package dev.echoellet.dragonfist_legacy.entity.shifu.combat

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.combat.ShifuPlayerTrainingPolicy.Companion.PLAYER_LOW_HEALTH_THRESHOLD
import dev.echoellet.dragonfist_legacy.entity.shifu.combat.ShifuPlayerTrainingPolicy.Companion.handlePlayerDeathDuringTraining
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageKeys
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageUtils
import dev.echoellet.dragonfist_legacy.util.getRandomFocusSeconds
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.living.LivingDamageEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import kotlin.math.sqrt

/**
 * Handles player–Shifu training interactions:
 *
 * - Determines whether a training session with the player can start. Prevents Shifu from
 *   harming the player if either is low on health and triggers the appropriate interactions.
 * - Determines whether an ongoing training session should continue. Pauses training and spares
 *   the player when low on health, providing the corresponding interactions.
 */
class ShifuPlayerTrainingPolicy(private val entity: ShifuEntity) {
    companion object {
        /**
         * The health value below which Shifu will normally stop training the player.
         *
         * Functionally, it acts as a safety threshold: if the player's health falls below this value,
         * Shifu will pause or stop the training session to avoid harming the student.
         *
         * Notes:
         * - Attack damage can be unpredictable and sometimes exceeds this threshold, for example, with the Epic Fight mod.
         *   Even if the player's health is above this value, a single attack could still kill them,
         *   which was originally an unintended bug.
         * - To work around this issue, the event in [PreDamageEventHandler] forces the player's health
         *   to stay at or above [PLAYER_SAFE_HEALTH] by clamping incoming damage from Shifu, preventing Shifu
         *   from killing the player during training.
         * - The method [handlePlayerDeathDuringTraining] is still needed to handle the case where the player dies from
         *   environmental hazards while fleeing.
         */
        private const val PLAYER_LOW_HEALTH_THRESHOLD = 6.0f

        /**
         * The health value used by [PreDamageEventHandler] to clamp incoming damage from Shifu.
         *
         * This value **must be slightly below [PLAYER_LOW_HEALTH_THRESHOLD]** so that:
         * 1. Shifu’s AI still recognizes the player as above the stop threshold and behaves correctly.
         * 2. The player cannot be reduced to fatal health by Shifu’s attacks during training.
         *
         * IMPORTANT: Not making this value at least slightly less than [PLAYER_LOW_HEALTH_THRESHOLD] will introduce a bug,
         * Shifu's AI will still target the player and continue attacking him but the player's health will never drop.
         *
         * The exact offset is minor and only serves as a safety buffer; the player’s health
         * will never drop below this value when attacked by Shifu.
         *
         * @see [PLAYER_LOW_HEALTH_THRESHOLD]
         */
        private const val PLAYER_SAFE_HEALTH = PLAYER_LOW_HEALTH_THRESHOLD - 0.1f

        private const val SHIFU_LOW_HEALTH_THRESHOLD = 50.0f

        private const val MIN_PLAYER_FOCUS_SECONDS = 4
        private const val MAX_PLAYER_FOCUS_SECONDS = 7

        private const val SHIFU_RETREAT_FROM_PLAYER_SPEED = 0.5
        private const val SHIFU_MAX_RETREAT_DISTANCE = 5.0

        /**
         * Handles cases where a player dies while fleeing from Shifu during training,
         * such as falling from height or swimming into lava.
         *
         * Background:
         * - The Epic Fight mod makes attack damage dynamic and unpredictable.
         *   Even when training is supposed to stop at low player health, a single attack
         *   could exceed that health and kill the player.
         * - With [PreDamageEventHandler] in place, Shifu’s attacks will never directly
         *   reduce the player below [PLAYER_SAFE_HEALTH].
         * - However, the game may still flag Shifu as the last attacker if the player
         *   dies from environmental hazards while fleeing.
         *
         * Implementation:
         * - Sends a thematic, in-character message to the player to acknowledge
         *   their death during training, maintaining immersion.
         *
         * @param player The player who died while fleeing Shifu.
         *
         * @see PLAYER_SAFE_HEALTH
         */
        fun handlePlayerDeathDuringTraining(player: Player) {
            // TODO: Consider adding a 2-second delay or notifying nearby players
            player.displayClientMessage(
                Component.translatable(
                    ShifuMessageKeys.SHIFU_ACCIDENTALLY_KILLED_PLAYER,
                    player.name
                ), false
            )
        }
    }

    /**
     * Determines whether Shifu can start a training session with the given player.
     *
     * Checks both the player's and Shifu's health, and any other
     * conditions that would prevent training from starting.
     *
     * @param player The player to check.
     * @return Whether training can be started with this [player].
     */
    fun canStartTraining(player: Player): Boolean = when (canStartTrainingWithReason(player)) {
        null -> true
        else -> false
    }

    /**
     * Handles an attempt by the given player to start training with Shifu.
     *
     * Checks whether training can start and displays an action bar message
     * if it cannot.
     *
     * **NOTE**: Callers are responsible for checking whether the given [player] is in creative mode.
     *
     * @param player The player attempting to train with Shifu.
     * @return Whether the training session can begin with this [player] (i.e., Whether Shifu can be damaged by this [player]).
     */
    fun handleStartTrainingAttempt(player: Player): Boolean {
        val reason = canStartTrainingWithReason(player) ?: return true

        val messageKey = getCannotStartTrainingActionBarMessageKey(reason)

        player.displayClientMessage(
            Component.translatable(messageKey), true,
        )
        retreatFromPlayer(player)

        return false
    }

    private fun retreatFromPlayer(player: Player) {
        val dx = entity.x - player.x
        val dz = entity.z - player.z
        var distance = sqrt(dx * dx + dz * dz)

        if (distance > 0.0) {
            // Limit the distance so Shifu doesn't move too far if player is very far
            if (distance > SHIFU_MAX_RETREAT_DISTANCE) {
                distance = SHIFU_MAX_RETREAT_DISTANCE
            }

            val motionX = (dx / distance) * SHIFU_RETREAT_FROM_PLAYER_SPEED
            val motionZ = (dz / distance) * SHIFU_RETREAT_FROM_PLAYER_SPEED

            entity.setDeltaMovement(
                motionX,
                entity.deltaMovement.y, // keep vertical motion intact
                motionZ
            )
        }
    }

    /**
     * Determines whether Shifu should continue training with the given player.
     *
     * Checks both the player's and Shifu's health, and any other
     * conditions that would require pausing or stopping training.
     *
     * @param player The player to check.
     * @return Whether training should continue with this [player].
     */
    fun handleTrainingContinuation(player: Player): Boolean {
        val reason = canStartTrainingWithReason(player) ?: return true

        val level = entity.level()
        val isRaining = level.isRaining

        val messageKey = when (reason) {
            CannotStartTrainingReason.ShifuLowHealth -> getRandomPlayerWinsMessageKey(isRaining = isRaining)
            CannotStartTrainingReason.PlayerLowHealth -> getRandomShifuWinsMessageKey(isRaining = isRaining)
        }

        stopTraining(
            player = player,
            message = Component.translatable(messageKey)
        )

        return false
    }

    /**
     * Stops the current training session with the given player.
     *
     * Clears Shifu's target, shows the appropriate message, and has Shifu
     * focus on the player for a short period of time.
     */
    private fun stopTraining(player: Player, message: Component) {
        // Unclear if resetting target is strictly needed, kept for safety.
        entity.target = null

        if (!player.isAlive) {
            /**
             * Shifu should never be able to kill the player because of [PreDamageEventHandler].
             * If the player somehow dies anyway, skip showing the message.
             */
            return
        }

        player.displayClientMessage(
            message, false
        )

        val focusSeconds =
            entity.random.getRandomFocusSeconds(MIN_PLAYER_FOCUS_SECONDS, MAX_PLAYER_FOCUS_SECONDS)
        entity.focusOnPlayer(player, focusSeconds)
    }

    /**
     * Determines the reason why Shifu cannot start training with the given player.
     *
     * Checks both the player's and Shifu's health, and other conditions
     * that would prevent training from starting.
     *
     * @param player The player to check.
     * @return The reason training cannot start, or null if training can start.
     */
    private fun canStartTrainingWithReason(player: Player): CannotStartTrainingReason? {
        val isPlayerOnLowHealth = isPlayerOnLowHealth(player.health)
        val isShifuOnLowHealth = isShifuOnLowHealth(entity.health)

        if (isPlayerOnLowHealth) {
            return CannotStartTrainingReason.PlayerLowHealth
        }
        if (isShifuOnLowHealth) {
            return CannotStartTrainingReason.ShifuLowHealth
        }

        return null
    }

    private fun getCannotStartTrainingActionBarMessageKey(reason: CannotStartTrainingReason): String = when (reason) {
        CannotStartTrainingReason.ShifuLowHealth -> ShifuMessageKeys.SHIFU_LOW_HEALTH
        CannotStartTrainingReason.PlayerLowHealth -> ShifuMessageKeys.PLAYER_LOW_HEALTH
    }

    private fun isPlayerOnLowHealth(health: Float): Boolean = health < PLAYER_LOW_HEALTH_THRESHOLD
    private fun isShifuOnLowHealth(health: Float): Boolean = health < SHIFU_LOW_HEALTH_THRESHOLD

    private fun getRandomShifuWinsMessageKey(isRaining: Boolean): String = ShifuMessageUtils.getRandomMessageKey(
        entity.random,
        ShifuMessageKeys.PlayerTraining.ShifuWins.NORMAL,
        ShifuMessageKeys.PlayerTraining.ShifuWins.RAIN,
        isRaining = isRaining,
    )

    private fun getRandomPlayerWinsMessageKey(isRaining: Boolean): String = ShifuMessageUtils.getRandomMessageKey(
        entity.random,
        ShifuMessageKeys.PlayerTraining.PlayerWins.NORMAL,
        ShifuMessageKeys.PlayerTraining.PlayerWins.RAIN,
        isRaining = isRaining,
    )

    /**
     * Prevents Shifu from killing or reducing the player's health below
     * [PLAYER_SAFE_HEALTH] during training.
     *
     * This event handler intercepts the damage calculation for players when
     * attacked by Shifu and clamps the damage so the player's health stays
     * at or above [PLAYER_SAFE_HEALTH], no matter how strong the attack is.
     *
     * This depends on the mod loader to work, as there is no reliable vanilla
     * Minecraft equivalent for safely intercepting pre-damage logic.
     *
     * @see [PLAYER_SAFE_HEALTH]
     */
    @Mod.EventBusSubscriber(modid = DragonFistLegacy.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    object PreDamageEventHandler {
        @SubscribeEvent
        fun onLivingDamage(event: LivingDamageEvent) {
            val target = event.entity
            val attacker = event.source.entity

            if (attacker !is ShifuEntity || target !is Player) return

            val currentHealth = target.health
            val minHealth = PLAYER_SAFE_HEALTH

            val dropsBelowMinHealth = currentHealth - event.amount < minHealth
            if (!dropsBelowMinHealth) return

            val clamped = (currentHealth - minHealth).coerceAtLeast(0f)
            event.amount = clamped
        }
    }
}

private enum class CannotStartTrainingReason {
    ShifuLowHealth,
    PlayerLowHealth,
}
