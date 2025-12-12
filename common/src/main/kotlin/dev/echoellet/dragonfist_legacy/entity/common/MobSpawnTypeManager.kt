package dev.echoellet.dragonfist_legacy.entity.common

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobSpawnType

class MobSpawnTypeManager(
    private val entity: Mob,
) {
    companion object {
        private const val KEY = "${DragonFistLegacy.ID}:spawn_type"
    }

    private var spawnType: MobSpawnType? = null

    /**
     * Returns the [MobSpawnType] for this [entity] (server-side only).
     *
     * This is set via [Mob.finalizeSpawn],
     * so it should not be called within that method,
     * instead using the provided method parameter.
     *
     * @return `null` if it could not be saved correctly or not loaded yet, otherwise [MobSpawnType]
     * @throw [IllegalArgumentException] if called on the logical client side
     */
    fun getMobSpawnType(): MobSpawnType? {
        require(!entity.level().isClientSide) { "Only the server can access the MobSpawnType" }
        return spawnType
    }

    fun saveNbt(compound: CompoundTag) {
        spawnType?.let { compound.putString(KEY, it.name) }
    }

    fun loadNbt(compound: CompoundTag) {
        if (!compound.contains(KEY)) {
            return
        }
        try {
            spawnType = MobSpawnType.valueOf(compound.getString(KEY))
        } catch (e: Exception) {
            DragonFistLegacy.LOGGER.error("Failed to read NBT '${KEY}' for entity: $entity\n${e.toString()}")
            compound.remove(KEY)
        }
    }

    fun onFinalizeSpawn(spawnType: MobSpawnType) {
        this.spawnType = spawnType
    }
}
