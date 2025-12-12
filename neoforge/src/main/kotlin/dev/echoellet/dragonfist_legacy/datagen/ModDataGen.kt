package dev.echoellet.dragonfist_legacy.datagen

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntitySpawns
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.biome.MobSpawnSettings
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers.AddSpawnsBiomeModifier
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.NeoForgeRegistries

@Suppress("unused") // Called by NeoForge data-gen
@EventBusSubscriber(modid = DragonFistLegacy.ID)
object ModDataGen {
    private val ADD_SPAWNS_BANDIT: ResourceKey<BiomeModifier> = ResourceKey.create(
        NeoForgeRegistries.Keys.BIOME_MODIFIERS,
        ResourceLocation.fromNamespaceAndPath(DragonFistLegacy.ID, "add_spawns_bandit"),
    )

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val builder = RegistrySetBuilder()

        builder.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS) { bootstrap ->
            val biomes = bootstrap.lookup(Registries.BIOME)

            val banditRegular = ModEntitySpawns.getBanditRegular()
            val banditEnforcer = ModEntitySpawns.getBanditEnforcer()
            bootstrap.register(
                ADD_SPAWNS_BANDIT,
                AddSpawnsBiomeModifier(
                    biomes.getOrThrow(ModEntitySpawns.SPAWN_BANDITS_TAG),
                    listOf(
                        MobSpawnSettings.SpawnerData(
                            banditRegular.entityType,
                            banditRegular.weight,
                            banditRegular.minGroupSize,
                            banditRegular.maxGroupSize,
                        ),
                        MobSpawnSettings.SpawnerData(
                            banditEnforcer.entityType,
                            banditEnforcer.weight,
                            banditEnforcer.minGroupSize,
                            banditEnforcer.maxGroupSize,
                        )
                    ),
                )
            )
        }
        event.createDatapackRegistryObjects(
            builder,
            mutableSetOf(DragonFistLegacy.ID),
        )
    }
}
