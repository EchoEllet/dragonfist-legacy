package dev.echoellet.dragonfist_legacy.entity.shifu.handlers

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag

class ShifuHomeTracker(private val entity: ShifuEntity) {
    companion object {
        private const val SPAWN_KEY = "${DragonFistLegacy.ID}:spawn_pos"
        private const val HOME_KEY = "${DragonFistLegacy.ID}:home_pos"
    }

    private var spawn: BlockPos? = null
    private var home: BlockPos? = null

    fun saveNbt(compound: CompoundTag) {
        spawn?.let { compound.putLong(SPAWN_KEY, it.asLong()) }
        home?.let { compound.putLong(HOME_KEY, it.asLong()) }
    }

    fun loadNbt(compound: CompoundTag) {
        spawn = loadPos(SPAWN_KEY, compound)
        home = loadPos(HOME_KEY, compound)
    }

    private fun loadPos(key: String, compound: CompoundTag): BlockPos {
        val fallbackPos = entity.blockPosition()
        if (!compound.contains(key)) {
            DragonFistLegacy.LOGGER.error(
                "NBT key '$key' missing for Shifu $entity. " +
                        "Defaulting to entity's current position $fallbackPos."
            )
            compound.putLong(key, fallbackPos.asLong())
            return fallbackPos
        }
        return try {
            BlockPos.of(compound.getLong(key))
        } catch (e: Exception) {
            DragonFistLegacy.LOGGER.error(
                "Failed to read NBT key '$key' for Shifu $entity. " +
                        "Resetting to current block position ${fallbackPos}. Exception: ${e.message}"
            )
            compound.remove(key)
            compound.putLong(key, fallbackPos.asLong())
            fallbackPos
        }
    }

    fun onFinalizeSpawn() {
        val position = entity.blockPosition()
        spawn = position
        home = position
    }

    val spawnPos: BlockPos get() = checkNotNull(spawn) { "The spawn position for Shifu $entity expected to be non-null." }
    val homePos: BlockPos get() = checkNotNull(home) { "The home position for Shifu $entity expected to be non-null." }
}
