package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.client.DragonFistLegacyNeoForgeClient
import dev.echoellet.dragonfist_legacy.compatibility.MinecraftMod
import dev.echoellet.dragonfist_legacy.compatibility.epicskills.EpicSkillsMod
import dev.echoellet.dragonfist_legacy.platform.NeoForgeModPlatform
import dev.echoellet.dragonfist_legacy.platform.registration.NeoForgeDeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.NeoForgeEntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.NeoForgeEntitySpawnRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.NeoForgeModRegistries
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent

@Mod(DragonFistLegacy.ID)
class DragonFistLegacyNeoForge(modEventBus: IEventBus, modContainer: ModContainer) {
    init {
        NeoForgeModRegistries.registerAll(modEventBus)
        DragonFistLegacy.initialize(
            NeoForgeModPlatform(
                deferredRegistrar = NeoForgeDeferredRegistrar(
                    creativeTab = NeoForgeModRegistries.CREATIVE_TAB,
                    item = NeoForgeModRegistries.ITEM,
                    entity = NeoForgeModRegistries.ENTITY,
                    sound = NeoForgeModRegistries.SOUND,
                ),
                entityAttributeRegistrar = NeoForgeEntityAttributeRegistrar()
                    .also { modEventBus.addListener<EntityAttributeCreationEvent> { event -> it.onCreation(event) } },
                entitySpawnRegistrar = NeoForgeEntitySpawnRegistrar()
                    .also { modEventBus.addListener<RegisterSpawnPlacementsEvent> { event -> it.onRegistration(event) } }
            )
        )
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC)
        registerModCompatibilities()

        if (FMLLoader.getDist().isClient) {
            loadClient(modEventBus, modContainer)
        }
    }

    private fun registerModCompatibilities() {
        if (MinecraftMod.EPIC_SKILLS.isLoaded()) {
            EpicSkillsMod.registerCompatibility()
        }
    }

    private fun loadClient(modEventBus: IEventBus, container: ModContainer) {
        DragonFistLegacyNeoForgeClient.initialize(modEventBus, container)
    }
}
