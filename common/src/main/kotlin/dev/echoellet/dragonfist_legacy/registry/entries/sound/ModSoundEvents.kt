package dev.echoellet.dragonfist_legacy.registry.entries.sound

import dev.echoellet.dragonfist_legacy.generated.SoundNames
import dev.echoellet.dragonfist_legacy.platform.ModPlatformProvider
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredSoundEvent

object ModSoundEvents {
    lateinit var SHIFU_AMBIENT: DeferredSoundEvent
    lateinit var BANDIT_MALE_AMBIENT: DeferredSoundEvent
    lateinit var BANDIT_FEMALE_AMBIENT: DeferredSoundEvent

    fun register() {
        SHIFU_AMBIENT = registerSound(SoundNames.ENTITY_SHIFU_AMBIENT)
        BANDIT_MALE_AMBIENT = registerSound(SoundNames.ENTITY_BANDIT_MALE_AMBIENT)
        BANDIT_FEMALE_AMBIENT = registerSound(SoundNames.ENTITY_BANDIT_FEMALE_AMBIENT)
    }

    private fun registerSound(name: String): DeferredSoundEvent {
        return ModPlatformProvider.get().deferredRegistrar().registerSound(name)
    }
}
