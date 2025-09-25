package dev.echoellet.dragonfist_legacy.entity.common

import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.LivingEntity

class RawEntityPropertyHandler(
    private val entity: LivingEntity,
    private val accessor: EntityDataAccessor<Int>,
    private val nbtKey: String,
) {
    private val _accessor
        get() = accessor

    fun defineDefault(defaultId: Int) {
        entity.entityData.define(_accessor, defaultId)
    }

    /** Saves to NBT */
    fun saveToNBT(compound: CompoundTag) {
        compound.putInt(nbtKey, getRaw())
    }

    /** Loads from NBT */
    fun loadFromNBT(compound: CompoundTag) {
        setRaw(compound.getInt(nbtKey))
    }

    fun getRaw(): Int = entity.entityData.get(_accessor)
    fun setRaw(id: Int) = entity.entityData.set(_accessor, id)
}
