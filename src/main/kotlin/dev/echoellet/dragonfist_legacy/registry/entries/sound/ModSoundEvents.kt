package dev.echoellet.dragonfist_legacy.registry.entries.sound

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.generated.SoundNames
import dev.echoellet.dragonfist_legacy.util.ModResources
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import java.util.function.Supplier

object ModSoundEvents {
    val REGISTRY: DeferredRegister<SoundEvent> = DeferredRegister.create(
        ForgeRegistries.SOUND_EVENTS,
        DragonFistLegacy.ID,
    )

    val SHIFU_AMBIENT = registerSound(SoundNames.ENTITY_SHIFU_AMBIENT)
    val BANDIT_MALE_AMBIENT = registerSound(SoundNames.ENTITY_BANDIT_MALE_AMBIENT)
    val BANDIT_FEMALE_AMBIENT = registerSound(SoundNames.ENTITY_BANDIT_FEMALE_AMBIENT)

    private fun registerSound(name: String): Supplier<SoundEvent> {
        val location = ModResources.id(name)
        return REGISTRY.register(
            name,
            Supplier { SoundEvent.createVariableRangeEvent(location) }
        )
    }
}
