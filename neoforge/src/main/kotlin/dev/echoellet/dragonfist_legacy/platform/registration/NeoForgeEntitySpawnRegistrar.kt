package dev.echoellet.dragonfist_legacy.platform.registration

import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.SpawnPlacementType
import net.minecraft.world.entity.SpawnPlacements
import net.minecraft.world.level.levelgen.Heightmap
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent

class NeoForgeEntitySpawnRegistrar : EntitySpawnRegistrar {
    private data class PendingEntry<T : Mob>(
        val entityType: DeferredEntity<T>,
        val spawnPlacementType: SpawnPlacementType,
        val heightmapType: Heightmap.Types,
        val predicate: SpawnPlacements.SpawnPredicate<T>,
    )

    private val pending = mutableListOf<PendingEntry<Mob>>()
    private var locked = false

    override fun <T : Mob> registerPlacement(
        entityType: DeferredEntity<T>,
        spawnPlacementType: SpawnPlacementType,
        heightmapType: Heightmap.Types,
        predicate: SpawnPlacements.SpawnPredicate<T>
    ) {
        require(!locked) {
            "Cannot register spawn placement after the 'RegisterSpawnPlacementsEvent' has fired. " +
                    "All spawn placement must be registered during mod setup, before the event is processed."
        }
        @Suppress("UNCHECKED_CAST")
        (entityType as DeferredEntity<Mob>)
        @Suppress("UNCHECKED_CAST")
        (predicate as SpawnPlacements.SpawnPredicate<Mob>)
        pending.add(
            PendingEntry(
                entityType,
                spawnPlacementType,
                heightmapType,
                predicate,
            )
        )
    }

    internal fun onRegistration(event: RegisterSpawnPlacementsEvent) {
        for (entry in pending) {
            event.register(
                entry.entityType.get(),
                entry.spawnPlacementType,
                entry.heightmapType,
                entry.predicate,
                RegisterSpawnPlacementsEvent.Operation.AND,
            )
        }
        locked = true
        pending.clear()
    }
}