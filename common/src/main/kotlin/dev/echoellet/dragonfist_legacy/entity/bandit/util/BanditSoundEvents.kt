package dev.echoellet.dragonfist_legacy.entity.bandit.util

import dev.echoellet.dragonfist_legacy.entity.common.gender.Gender
import dev.echoellet.dragonfist_legacy.registry.entries.sound.ModSoundEvents
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents

object BanditSoundEvents {

    fun getByGender(gender: Gender): BanditSound = when(gender) {
        Gender.Male -> Male
        Gender.Female -> Female
    }

    object Male: BanditSound {
        override fun getAmbientSound(): SoundEvent {
            return ModSoundEvents.BANDIT_MALE_AMBIENT.get()
        }
    }

    object Female: BanditSound {
        override fun getAmbientSound(): SoundEvent {
            return ModSoundEvents.BANDIT_FEMALE_AMBIENT.get()
        }
    }
}

interface BanditSound {
    fun getAmbientSound(): SoundEvent

    fun getHurtSound(): SoundEvent = SoundEvents.GENERIC_HURT

    fun getDeathSound(): SoundEvent = SoundEvents.GENERIC_DEATH
}
