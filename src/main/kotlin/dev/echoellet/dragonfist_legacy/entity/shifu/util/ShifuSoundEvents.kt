package dev.echoellet.dragonfist_legacy.entity.shifu.util

import dev.echoellet.dragonfist_legacy.registry.entries.sound.ModSoundEvents
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents

object ShifuSoundEvents {
    fun getAmbientSound(): SoundEvent = ModSoundEvents.SHIFU_AMBIENT.get()

    fun getHurtSound(): SoundEvent = SoundEvents.GENERIC_HURT

    fun getDeathSound(): SoundEvent = SoundEvents.GENERIC_DEATH
}
