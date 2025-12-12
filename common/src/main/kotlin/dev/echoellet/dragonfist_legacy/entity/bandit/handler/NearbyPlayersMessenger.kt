package dev.echoellet.dragonfist_legacy.entity.bandit.handler

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

/**
 * Sends a localized chat message to all players within a fixed radius around a given entity.
 *
 * This is useful for sending combat, event, or boss messages to nearby players.
 *
 * @property NEARBY_PLAYER_RADIUS The radius (in blocks) around the entity to search for players.
 */
object NearbyPlayersMessenger {
    /** Radius (in blocks) around the mob to check for nearby players. */
    private const val NEARBY_PLAYER_RADIUS = 16.0

    /**
     * Sends a translated message to all players within [NEARBY_PLAYER_RADIUS] of the given entity.
     *
     * @param messageKey The translation key of the message to send (used with `Component.translatable`).
     * @param entity The entity around which nearby players will be notified.
     */
    fun sendMessage(
        messageKey: String,
        entity: LivingEntity,
    ) {
        val nearbyPlayers = entity.level().getEntitiesOfClass(
            Player::class.java,
            entity.boundingBox.inflate(NEARBY_PLAYER_RADIUS),
        )
        if (nearbyPlayers.isEmpty()) return

        nearbyPlayers.forEach { player ->
            player.displayClientMessage(Component.translatable(messageKey), false)
        }
    }
}
