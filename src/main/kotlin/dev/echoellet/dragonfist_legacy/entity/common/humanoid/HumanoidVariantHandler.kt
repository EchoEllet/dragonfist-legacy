package dev.echoellet.dragonfist_legacy.entity.common.humanoid

import dev.echoellet.dragonfist_legacy.entity.common.RawEntityPropertyHandler
import dev.echoellet.dragonfist_legacy.util.minecraftRandom
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.world.entity.LivingEntity

class HumanoidVariantHandler(
    private val entity: LivingEntity,
    private val accessor: EntityDataAccessor<Int>
) {
    companion object {
        private const val KEY = "Variant"
        private val DEFAULT = HumanoidVariant.DEFAULT.id
    }

    private val propertyHandler = RawEntityPropertyHandler(
        entity = entity,
        accessor = accessor,
        nbtKey = KEY,
    )

    fun defineDefault() = propertyHandler.defineDefault(DEFAULT)

    fun saveToNBT(compound: CompoundTag) = propertyHandler.saveToNBT(compound)

    fun loadFromNBT(compound: CompoundTag) = propertyHandler.loadFromNBT(compound)

    fun setRandomVariant() {
        val random = HumanoidVariant.entries.minecraftRandom(entity.random)
        setVariant(random)
    }

    fun getVariant(): HumanoidVariant = HumanoidVariant.fromId(propertyHandler.getRaw())
    private fun setVariant(gender: HumanoidVariant): Unit = propertyHandler.setRaw(gender.id)
}
