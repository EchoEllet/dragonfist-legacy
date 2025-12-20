package dev.echoellet.dragonfist_legacy.entity.common

import dev.echoellet.dragonfist_legacy.entity.common.CombatMessageSequencer.Companion.INITIAL_DELAY_TICKS
import dev.echoellet.dragonfist_legacy.util.secondsToTicks
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player

/**
 * Handles sending a sequence of combat messages from a Mob to nearby players.
 *
 * Messages are sent in order, one at a time, with a random delay between them.
 * Only players within a fixed radius ([NearbyPlayersMessenger.NEARBY_PLAYER_RADIUS]) around the entity will receive the messages.
 *
 * The sequence pauses if there are no messages left or if the entity has no valid target,
 * and resumes where it left off when the entity is back in combat with the player.
 *
 * The first message is always sent after [INITIAL_DELAY_TICKS].
 *
 * Since Minecraft entities usually have one target at a time, this ensures important NPCs will send
 * the message to nearby players that are fighting with the player, which is useful for boss fight.
 *
 * @param entity The Mob sending the messages.
 * @param messages The ordered list of message keys to send during combat.
 */
class CombatMessageSequencer(
    private val entity: Mob,
    private val getTarget: () -> LivingEntity?,
    private val messages: List<String>,
) {
    companion object {
        /** Initial delay before sending the first message, in ticks. */
        private val INITIAL_DELAY_TICKS = 3.secondsToTicks

        /** Minimum and maximum delay (in ticks) between consecutive messages. */
        private object NextMessageDelayTicks {
            val MIN = 6.secondsToTicks
            val MAX = 16.secondsToTicks
        }
    }

    /** Number of ticks since the last message was sent. */
    private var ticksSinceLastMessageSent = 0

    /** Index of the next message to send in [messages]. */
    private var currentMessageIndex = 0

    /** Number of ticks to wait before sending the next message. */
    private var nextMessageTicks = INITIAL_DELAY_TICKS

    /**
     * Called every tick to potentially send a message during the combat.
     *
     * Sends the next message to all players within [NearbyPlayersMessenger.NEARBY_PLAYER_RADIUS] if the delay has passed.
     * Pauses if there are no messages left or the entity no longer have a target.
     *
     * Must be called **only** on the server side.
     */
    fun tick() {
        val target = getTarget() ?: return
        if (target !is Player) return

        val noMoreMessages = currentMessageIndex >= messages.size

        if (noMoreMessages) return

        ticksSinceLastMessageSent++

        if (ticksSinceLastMessageSent >= nextMessageTicks) {
            ticksSinceLastMessageSent = 0
            sendCurrentMessages()

            currentMessageIndex++

            nextMessageTicks = getRandomMessageDelayTicks()
        }
    }

    /**
     * Sends the current message to all players within [NearbyPlayersMessenger.NEARBY_PLAYER_RADIUS] of the entity.
     */
    private fun sendCurrentMessages() {
        val messageKey = messages[currentMessageIndex]

        NearbyPlayersMessenger.sendMessage(messageKey, entity)
    }

    /**
     * Generates a random delay in ticks for the next message,
     * between [NextMessageDelayTicks.MIN] and [NextMessageDelayTicks.MAX].
     */
    private fun getRandomMessageDelayTicks() =
        entity.random.nextInt(NextMessageDelayTicks.MIN, NextMessageDelayTicks.MAX + 1)
}
