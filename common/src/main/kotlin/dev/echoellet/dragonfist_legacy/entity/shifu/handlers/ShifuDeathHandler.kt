package dev.echoellet.dragonfist_legacy.entity.shifu.handlers

import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.util.secondsToTicks
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.LevelAccessor

class ShifuDeathHandler(private val dyingEntity: ShifuEntity) {
    companion object {
        private const val NOTIFICATION_RADIUS = 100.0
        private val WEATHER_TIME = (5 * 60).secondsToTicks // 5 Minutes
    }

    fun handle(
        sourceEntity: Entity?,
        killerMessageKey: String,
        naturalMessageKey: String,
    ) {
        val level = dyingEntity.level()
        if (level.isClientSide) {
            return
        }

        sendNotification(
            sourceEntity = sourceEntity,
            killerMessageKey = killerMessageKey,
            naturalMessageKey = naturalMessageKey,
        )

        triggerDramaticWeather()
    }

    private fun sendNotification(
        sourceEntity: Entity?,
        killerMessageKey: String,
        naturalMessageKey: String,
    ) {
        val level: LevelAccessor = dyingEntity.level()
        if (level.server != null) {
            val killerPlayer = sourceEntity as? Player
            val message: Component = if (killerPlayer != null) {
                Component.translatable(killerMessageKey, killerPlayer.name)
            } else {
                Component.translatable(naturalMessageKey)
            }

            // Send to nearby players
            level.players().filter { it.distanceToSqr(dyingEntity) <= NOTIFICATION_RADIUS * NOTIFICATION_RADIUS }
                .forEach { it.sendSystemMessage(message) }
        }
    }

    private fun triggerDramaticWeather() {
        val level = dyingEntity.level()

        val serverLevel = level as ServerLevel? ?: return

        if (serverLevel.isRaining && serverLevel.isThundering) {
            return
        }

        serverLevel.setWeatherParameters(
            0,
            WEATHER_TIME,
            true,
            true
        )
    }
}
