package dev.echoellet.dragonfist_legacy.platform.registration

import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.SpawnPlacementType
import net.minecraft.world.entity.SpawnPlacements
import net.minecraft.world.level.levelgen.Heightmap

class FabricEntitySpawnRegistrar : EntitySpawnRegistrar {
    override fun <T : Mob> registerPlacement(
        entityType: DeferredEntity<T>,
        spawnPlacementType: SpawnPlacementType,
        heightmapType: Heightmap.Types,
        predicate: SpawnPlacements.SpawnPredicate<T>
    ) {
        SpawnPlacements.register(entityType.get(), spawnPlacementType, heightmapType, predicate)
    }
}
