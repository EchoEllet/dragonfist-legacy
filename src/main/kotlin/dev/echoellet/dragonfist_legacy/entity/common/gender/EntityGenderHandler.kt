package dev.echoellet.dragonfist_legacy.entity.common.gender

import dev.echoellet.dragonfist_legacy.entity.common.RawEntityPropertyHandler
import dev.echoellet.dragonfist_legacy.util.minecraftRandom
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.LivingEntity

/**
 * IMPORTANT: When creating an instance of this class within an entity class,
 * it must be done inside [net.minecraft.world.entity.Entity.defineSynchedData] because
 * `defineSynchedData` is called during the constructor of [net.minecraft.world.entity.Entity].
 * At that point, other class-level properties are not yet initialized,
 * so creating it earlier could cause a [NullPointerException].
 *
 * Initializing in `defineSynchedData` ensures the instance exists when needed for defining synched data,
 * while respecting Minecraft’s construction order.
 */
class EntityGenderHandler(
    private val entity: LivingEntity,
    private val accessor: EntityDataAccessor<Int>
) {
    companion object {
        private const val KEY = "Gender"
        private val DEFAULT = Gender.DEFAULT.id
    }

    private val propertyHandler = RawEntityPropertyHandler(
        entity = entity,
        accessor = accessor,
        nbtKey = KEY,
    )

    fun defineDefault() = propertyHandler.defineDefault(DEFAULT)

    fun saveToNBT(compound: CompoundTag) = propertyHandler.saveToNBT(compound)

    fun loadFromNBT(compound: CompoundTag) = propertyHandler.loadFromNBT(compound)

    fun setRandomGender() {
        val random = Gender.entries.minecraftRandom(entity.random)
        setGender(random)
    }

    fun getGender(): Gender = Gender.fromId(propertyHandler.getRaw())
    private fun setGender(gender: Gender): Unit = propertyHandler.setRaw(gender.id)
}
