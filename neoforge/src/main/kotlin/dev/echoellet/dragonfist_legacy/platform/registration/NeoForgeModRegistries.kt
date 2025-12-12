package dev.echoellet.dragonfist_legacy.platform.registration

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.core.registries.Registries
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object NeoForgeModRegistries {
    val CREATIVE_TAB: DeferredRegister<CreativeModeTab> = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB,
        DragonFistLegacy.ID
    )
    val ITEM: DeferredRegister.Items = DeferredRegister.createItems(DragonFistLegacy.ID)
    val ENTITY: DeferredRegister<EntityType<*>> = DeferredRegister.create(
        Registries.ENTITY_TYPE, DragonFistLegacy.ID
    )
    val SOUND: DeferredRegister<SoundEvent> = DeferredRegister.create(
        Registries.SOUND_EVENT,
        DragonFistLegacy.ID,
    )

    private val REGISTRIES: Set<DeferredRegister<*>> = setOf(CREATIVE_TAB, ITEM, ENTITY, SOUND)

    fun registerAll(bus: IEventBus) {
        for (registry in REGISTRIES) {
            registry.register(bus)
        }
    }
}