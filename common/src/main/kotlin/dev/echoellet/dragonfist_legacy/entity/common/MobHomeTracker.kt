package dev.echoellet.dragonfist_legacy.entity.common

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.util.constants.Constants
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Mob

/**
 * @param storeOriginalSpawnPos Whether to spawn the original [entity]'s spawn position,
 * which cannot be changed, unlike [homePos] (both defaults to the same position).
 */
class MobHomeTracker(
    private val entity: Mob,
    private val storeOriginalSpawnPos: Boolean,
) {
    companion object {
        private const val ORIGINAL_SPAWN_KEY = "${Constants.NBT_KEY_PREFIX}:spawn_pos"
        private const val HOME_KEY = "${Constants.NBT_KEY_PREFIX}:home_pos"
    }

    private var originalSpawn: BlockPos? = null
    private var home: BlockPos? = null

    fun saveNbt(compound: CompoundTag) {
        if (storeOriginalSpawnPos) {
            originalSpawn?.let { compound.putLong(ORIGINAL_SPAWN_KEY, it.asLong()) }
        }
        home?.let { compound.putLong(HOME_KEY, it.asLong()) }
    }

    fun loadNbt(compound: CompoundTag) {
        if (storeOriginalSpawnPos) {
            originalSpawn = loadPos(ORIGINAL_SPAWN_KEY, compound)
        }
        home = loadPos(HOME_KEY, compound)
    }

    private fun loadPos(key: String, compound: CompoundTag): BlockPos {
        val fallbackPos = entity.blockPosition()
        if (!compound.contains(key)) {
            DragonFistLegacy.LOGGER.debug(
                "NBT key '$key' missing for mob $entity. " +
                        "Defaulting to entity's current position $fallbackPos. " +
                "This issue may happen if the NBT key was manually removed. " +
                "Ignore this message if this entity was spawned via '/summon' command (known Minecraft limitation)."

            )
            compound.putLong(key, fallbackPos.asLong())
            return fallbackPos
        }
        return try {
            BlockPos.of(compound.getLong(key))
        } catch (e: Exception) {
            DragonFistLegacy.LOGGER.error(
                "Failed to read NBT key '$key' for mob $entity. " +
                        "Resetting to current block position ${fallbackPos}. Exception: ${e.message}"
            )
            compound.remove(key)
            compound.putLong(key, fallbackPos.asLong())
            fallbackPos
        }
    }

    fun setCurrentPosition() {
        val position = entity.blockPosition()
        if (storeOriginalSpawnPos) {
            originalSpawn = position
        }
        home = position
    }

    val originalSpawnPos: BlockPos
        get() {
            if (!storeOriginalSpawnPos) {
                throw IllegalStateException("The original spawn position for entity of type `$entity` is not intended to be stored.")
            }
            return checkNotNull(originalSpawn) { "The spawn position for mob $entity expected to be non-null." }
        }
    val homePos: BlockPos get() = checkNotNull(home) { "The home position for mob $entity expected to be non-null." }
}
