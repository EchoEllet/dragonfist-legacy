package dev.echoellet.dragonfist_legacy.entity.knight

import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents

object KnightSoundEvents {
    fun getHurtSound(): SoundEvent = SoundEvents.GENERIC_HURT

    fun getDeathSound(): SoundEvent = SoundEvents.GENERIC_DEATH

    fun getStepSound(): SoundEvent = SoundEvents.IRON_GOLEM_STEP
}
