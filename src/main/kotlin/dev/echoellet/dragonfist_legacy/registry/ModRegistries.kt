package dev.echoellet.dragonfist_legacy.registry

import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.registry.entries.item.ModCreativeModeTabs
import dev.echoellet.dragonfist_legacy.registry.entries.item.ModItems
import dev.echoellet.dragonfist_legacy.registry.entries.sound.ModSoundEvents
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object ModRegistries {
    private val REGISTRIES: List<DeferredRegister<out Any>> = listOf(
        ModCreativeModeTabs.REGISTRY,
        ModItems.REGISTRY,
        ModEntities.REGISTRY,
        ModSoundEvents.REGISTRY,
    )

    fun registerAll(bus: IEventBus) {
        for (registry in REGISTRIES) {
            registry.register(bus)
        }
    }
}
