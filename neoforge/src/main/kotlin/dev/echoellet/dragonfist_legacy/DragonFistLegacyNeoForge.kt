package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.client.DragonFistLegacyNeoForgeClient
import dev.echoellet.dragonfist_legacy.compatibility.IntegrationEntrypoint
import dev.echoellet.dragonfist_legacy.compatibility.MinecraftMod
import dev.echoellet.dragonfist_legacy.compatibility.epicfight.EpicFightModEntrypoint
import dev.echoellet.dragonfist_legacy.compatibility.epicskills.EpicSkillsModEntrypoint
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
        registerExternalModIntegrations()

        if (FMLLoader.getDist().isClient) {
            loadClient(modEventBus, modContainer)
        }
    }

    private fun registerExternalModIntegrations() {
        for (mod in MinecraftMod.entries) {
            if (!mod.isLoaded()) {
                continue
            }
            val integration: IntegrationEntrypoint = when (mod) {
                MinecraftMod.EPIC_FIGHT -> EpicFightModEntrypoint()
                MinecraftMod.EPIC_SKILLS -> EpicSkillsModEntrypoint()
            }
            integration.onInitialize()
        }
    }

    private fun loadClient(modEventBus: IEventBus, container: ModContainer) {
        DragonFistLegacyNeoForgeClient.initialize(modEventBus, container)
    }
}
