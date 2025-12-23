package dev.echoellet.dragonfist_legacy.entity.common

import dev.echoellet.dragonfist_legacy.entity.common.EntityPassiveHpRegenManager.Companion.HEAL_PER_INTERVAL
import dev.echoellet.dragonfist_legacy.entity.common.EntityPassiveHpRegenManager.Companion.TICKS_BETWEEN_HEAL
import dev.echoellet.dragonfist_legacy.util.secondsToTicks
import net.minecraft.world.entity.LivingEntity

// TODO: Refactor to be vanilla [Goal] ?

class EntityPassiveHpRegenManager(
    private val entity: LivingEntity,
    private val isInCombat: () -> Boolean
) {
    companion object {
        /** Number of ticks to wait after leaving combat before regen starts (~10 seconds) */
        private val OUT_OF_COMBAT_TIMEOUT_TICKS = 10.secondsToTicks

        /**
         * Number of ticks between each healing action.
         * Heals every second instead of every tick to reduce network calls.
         */
        private val TICKS_BETWEEN_HEAL = 1.secondsToTicks

        /**
         * Amount of health restored per interval.
         * Formula: base heal rate per tick * [TICKS_BETWEEN_HEAL]
         * This ensures the entity heals correctly according to the interval.
         */
        private val HEAL_PER_INTERVAL = 0.1f * TICKS_BETWEEN_HEAL
    }

    /** Ticks spent out of combat */
    private var outOfCombatTicks = 0

    /** Tick counter for interval-based healing */
    private var tickCounter = 0

    /** Called every tick to potentially heal the entity. Must be called **only** on the server side. */
    fun tick() {
        if (entity.health >= entity.maxHealth) return

        if (isInCombat()) {
            stopPassiveRegen()
            return
        }

        outOfCombatTicks++
        if (outOfCombatTicks < OUT_OF_COMBAT_TIMEOUT_TICKS) return

        tickCounter++
        if (tickCounter < TICKS_BETWEEN_HEAL) return
        tickCounter = 0

        heal()
    }

    /** Called when the entity takes damage to reset timers */
    fun onHurt() {
        // Prevents rare bugs at very low health where the entity appears "half-dead"
        // but remains alive due to tiny remaining health (because of passive healing).
        // Resets out-of-combat timer.
        stopPassiveRegen()
    }

    /** Heal the entity by [HEAL_PER_INTERVAL], clamped at max health */
    private fun heal() {
        val newHealth = (entity.health + HEAL_PER_INTERVAL).coerceAtMost(entity.maxHealth)
        entity.health = newHealth
    }

    /** Resets counters to stop passive regeneration */
    private fun stopPassiveRegen() {
        outOfCombatTicks = 0
        tickCounter = 0
    }
}
