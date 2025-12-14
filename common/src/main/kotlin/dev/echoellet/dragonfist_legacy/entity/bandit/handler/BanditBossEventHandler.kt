package dev.echoellet.dragonfist_legacy.entity.bandit.handler

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerBossEvent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.BossEvent

class BanditBossEventHandler(
    private val entity: BanditEntity,
    val color: BossEvent.BossBarColor,
    private val bossNameKey: () -> String,
) {
    private lateinit var bossEvent: ServerBossEvent

    fun initializeBossEvent() {
        if (!::bossEvent.isInitialized) {
            // Lazily create bossEvent if not already initialized
            bossEvent = createBossEvent()
        }
    }

    fun updateHealth() = bossEvent.setProgress(entity.health / entity.maxHealth)

    fun addPlayer(player: ServerPlayer) = bossEvent.addPlayer(player)
    fun removePlayer(player: ServerPlayer) = bossEvent.removePlayer(player)

    private fun createBossEvent(): ServerBossEvent {
        return ServerBossEvent(
            Component.translatable(bossNameKey()),
            color,
            BossEvent.BossBarOverlay.PROGRESS,
        )
    }
}
