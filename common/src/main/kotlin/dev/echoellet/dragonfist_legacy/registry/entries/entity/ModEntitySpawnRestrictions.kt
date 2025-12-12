package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.platform.ModPlatformProvider
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.SpawnPlacementType
import net.minecraft.world.entity.SpawnPlacementTypes
import net.minecraft.world.entity.SpawnPlacements
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.levelgen.Heightmap

object ModEntitySpawnRestrictions {
    fun register() {
        registerBandits()
    }

    private fun registerBandits() {
        registerBandit(ModEntities.BANDIT_REGULAR)
        registerBandit(ModEntities.BANDIT_ENFORCER)
    }

    private fun <T : BanditEntity> registerBandit(entityType: DeferredEntity<T>) {
        registerPlacement(
            entityType,
            SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
        ) { entityType, level, spawnType, pos, random ->
            level.difficulty != Difficulty.PEACEFUL &&
                    Monster.isDarkEnoughToSpawn(level, pos, random) &&
                    Mob.checkMobSpawnRules(entityType, level, spawnType, pos, random)
        }
    }

    private fun <T : Mob> registerPlacement(
        entityType: DeferredEntity<T>,
        spawnPlacementType: SpawnPlacementType,
        heightmapType: Heightmap.Types,
        predicate: SpawnPlacements.SpawnPredicate<T>
    ) {
        ModPlatformProvider.get().entitySpawnRegistrar()
            .registerPlacement(entityType, spawnPlacementType, heightmapType, predicate)
    }
}
