package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.platform.FabricModPlatform
import dev.echoellet.dragonfist_legacy.platform.registration.FabricDeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.FabricEntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.FabricEntitySpawnRegistrar
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntitySpawns
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

        addEntitySpawns()
    }

    // Could not be fully shared in "common", as entity spawn
    // registrations are data-driven in Forge-like platforms.
    private fun addEntitySpawns() {
        val banditRegular = ModEntitySpawns.getBanditRegular()
        val banditEnforcer = ModEntitySpawns.getBanditEnforcer()
        val spawnBanditsTag = BiomeSelectors.tag(ModEntitySpawns.SPAWN_BANDITS_TAG)
        BiomeModifications.addSpawn(
            spawnBanditsTag,
            MobCategory.MONSTER,
            banditRegular.entityType,
            banditRegular.weight,
            banditRegular.minGroupSize,
            banditRegular.maxGroupSize,
        )
        BiomeModifications.addSpawn(
            spawnBanditsTag,
            MobCategory.MONSTER,
            banditEnforcer.entityType,
            banditEnforcer.weight,
            banditEnforcer.minGroupSize,
            banditEnforcer.maxGroupSize,
        )
    }
}
