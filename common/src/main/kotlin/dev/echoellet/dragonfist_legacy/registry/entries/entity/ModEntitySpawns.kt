package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.biome.Biome

// Note: The entity spawn registrations could not be shared in "common",
// as they are fully data-driven in Forge-like platforms.
object ModEntitySpawns {
    data class Spawn(
        val entityType: EntityType<*>,
        val weight: Int,
        val minGroupSize: Int,
        val maxGroupSize: Int
    )

    val SPAWN_BANDITS_TAG: TagKey<Biome> = TagKey.create(
        Registries.BIOME,
        DragonFistLegacy.identifier("spawn_bandits"),
    )

    fun getBanditRegular(): Spawn {
        return Spawn(
            ModEntities.BANDIT_REGULAR.get(),
            16,
            1,
            6,
        )
    }

    fun getBanditEnforcer(): Spawn {
        return Spawn(
            ModEntities.BANDIT_ENFORCER.get(),
            8,
            1,
            3,
        )
    }
}
