package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.platform.FabricModPlatform
import dev.echoellet.dragonfist_legacy.platform.registration.FabricDeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.FabricEntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.FabricEntitySpawnRegistrar
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.world.entity.MobCategory
import net.neoforged.fml.config.ModConfig

object DragonFistLegacyFabric : ModInitializer {
    override fun onInitialize() {
        DragonFistLegacy.initialize(
            FabricModPlatform(
                deferredRegistrar = FabricDeferredRegistrar(),
                entityAttributeRegistrar = FabricEntityAttributeRegistrar(),
                entitySpawnRegistrar = FabricEntitySpawnRegistrar(),
            )
        )
        NeoForgeConfigRegistry.INSTANCE.register(DragonFistLegacy.ID, ModConfig.Type.SERVER, Config.SPEC)

        addSpawns()
    }

    private fun addSpawns() {
        // TODO: (FABRIC_PORT) Keep it consistent with NeoForge's add_bandit_spawns.json (maybe share it in common module by avoiding NeoForge's datapack?)
        BiomeModifications.addSpawn(
            BiomeSelectors.foundInOverworld(),
            MobCategory.MONSTER,
            ModEntities.BANDIT_REGULAR.get(),
            16,
            1,
            6,
        )
        BiomeModifications.addSpawn(
            BiomeSelectors.foundInOverworld(),
            MobCategory.MONSTER,
            ModEntities.BANDIT_ENFORCER.get(),
            8,
            1,
            3,
        )
    }
}
