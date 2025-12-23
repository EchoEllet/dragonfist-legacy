package dev.echoellet.dragonfist_legacy.entity.common

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.util.constants.Constants
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobSpawnType as EntitySpawnReason

// Note: Mojang renamed "MobSpawnType" to "EntitySpawnReason" in 1.21.11+
class MobSpawnReasonManager(
    private val entity: Mob,
) {
    companion object {
        private const val KEY = "${Constants.NBT_KEY_PREFIX}:spawn_reason"
    }

    private var spawnReason: EntitySpawnReason? = null

    /**
     * Returns the [EntitySpawnReason] for this [entity] (server-side only).
     *
     * This is set via [Mob.finalizeSpawn],
     * so it should not be called within that method,
     * instead using the provided method parameter.
     *
     * @return `null` if it could not be saved correctly or not loaded yet, otherwise [EntitySpawnReason]
     * @throw [IllegalArgumentException] if called on the logical client side
     */
    fun getMobSpawnReason(): EntitySpawnReason? {
        require(!entity.level().isClientSide) { "Only the server can access the EntitySpawnReason" }
        return spawnReason
    }

    fun isSpawnReasonKnown(): Boolean {
        return getMobSpawnReason() != null
    }

    fun saveNbt(compound: CompoundTag) {
        spawnReason?.let { compound.putString(KEY, it.name) }
    }

    fun loadNbt(compound: CompoundTag) {
        if (!compound.contains(KEY)) {
            DragonFistLegacy.LOGGER.warn(
                "NBT key '$KEY' missing for mob $entity. The mob spawn reason is unknown if this entity was not summoned via command. " +
                "Ignore this message if the entity was spawned via '/summon' command (known Minecraft limitation)."
            )
            return
        }
        try {
            spawnReason = EntitySpawnReason.valueOf(compound.getString(KEY))
        } catch (e: Exception) {
            DragonFistLegacy.LOGGER.error("Failed to read NBT '${KEY}' for entity: $entity\n${e.toString()}")
            compound.remove(KEY)
        }
    }

    fun setSpawnReason(spawnReason: EntitySpawnReason) {
        this.spawnReason = spawnReason
    }
}
