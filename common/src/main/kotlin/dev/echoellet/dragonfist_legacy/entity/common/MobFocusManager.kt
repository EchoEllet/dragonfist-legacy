package dev.echoellet.dragonfist_legacy.entity.common

import dev.echoellet.dragonfist_legacy.util.MathUtils
import dev.echoellet.dragonfist_legacy.util.secondsToTicks
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import kotlin.math.atan2

/**
 * Allows a mob to focus on a living entity (updating the rotation without moving).
 */
class MobFocusManager(private val entity: Mob) {

    companion object {
        private const val RAD_TO_DEG = (180.0 / Math.PI).toFloat()
    }

    private var focusTarget: LivingEntity? = null
    private var remainingFocusTicks: Int? = null

    fun focusOnEntity(entity: LivingEntity, durationSeconds: Int? = null) {
        focusTarget = entity
        durationSeconds?.let { remainingFocusTicks = it.secondsToTicks }
    }

    /**
     * Called to reset the current focus target. Typically called in [Mob.hurt].
     * Must be called **only** on the logical server side.
     */
    fun clearFocus() {
        focusTarget = null
        remainingFocusTicks = null
    }

    /** Called on every tick to update the focus. Must be called **only** on the logical server side. */
    fun updateFocus() {
        val target = focusTarget ?: return
        remainingFocusTicks?.let {
            if (it <= 0) {
                clearFocus()
                return
            }

            remainingFocusTicks = it - 1
        }

        // Smoothly rotate body and head toward player

        entity.navigation.stop()

        val dx = target.x - entity.x
        val dz = target.z - entity.z

        if (dx != 0.0 || dz != 0.0) {
            val desiredYaw = atan2(dz, dx).toFloat() * RAD_TO_DEG - 90f

            entity.yBodyRot = MathUtils.rotLerp(entity.yBodyRot, desiredYaw, 10f)
            entity.yRot = entity.yBodyRot
            entity.yHeadRot = entity.yBodyRot
        }
    }

    fun getTarget(): LivingEntity? = focusTarget
}
