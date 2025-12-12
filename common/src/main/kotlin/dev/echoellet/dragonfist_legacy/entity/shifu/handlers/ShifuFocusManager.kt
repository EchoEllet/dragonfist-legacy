package dev.echoellet.dragonfist_legacy.entity.shifu.handlers

import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.util.MathUtils
import dev.echoellet.dragonfist_legacy.util.secondsToTicks
import net.minecraft.world.entity.player.Player
import kotlin.math.atan2

class ShifuFocusManager(private val entity: ShifuEntity) {

    companion object {
        private const val RAD_TO_DEG = (180.0 / Math.PI).toFloat()
    }

    private var focusTarget: Player? = null
    private var focusTicks: Int = 0

    fun focusOnPlayer(player: Player, durationSeconds: Int) {
        focusTarget = player
        focusTicks = durationSeconds.secondsToTicks
    }

    fun clearFocus() {
        focusTarget = null
        focusTicks = 0
    }

    /** Called on every tick to update the focus. Must be called **only** on the server side. */
    fun updateFocus() {
        if (focusTicks <= 0 && focusTarget != null) {
            clearFocus()
            return
        }

        // Smoothly rotate body and head toward player

        focusTarget?.let { target ->
            entity.navigation.stop()

            val dx = target.x - entity.x
            val dz = target.z - entity.z

            if (dx != 0.0 || dz != 0.0) {
                val desiredYaw = atan2(dz, dx).toFloat() * RAD_TO_DEG - 90f

                entity.yBodyRot = MathUtils.rotLerp(entity.yBodyRot, desiredYaw, 10f)
                entity.yRot = entity.yBodyRot
                entity.yHeadRot = entity.yBodyRot
            }

            focusTicks--
        }
    }
}
