package dev.echoellet.dragonfist_legacy.entity.bandit.rank.regular

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.common.humanoid.HumanoidVariant
import dev.echoellet.dragonfist_legacy.entity.common.humanoid.HumanoidVariantHandler
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

class BanditRegularEntity(
    type: EntityType<BanditRegularEntity>,
    world: Level,
) : BanditEntity(type, world) {
    override fun getRank(): BanditRank = BanditRank.Regular

    companion object {
        fun createAttributes() = createBaseAttributes(
            maxHealth = 20.0,
            movementSpeed = 0.3,
            stepHeight = 3.0
        )

        private val VARIANT_ACCESSOR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(BanditRegularEntity::class.java, EntityDataSerializers.INT)
    }

    private lateinit var variantHandler: HumanoidVariantHandler

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)

        variantHandler = HumanoidVariantHandler(this, VARIANT_ACCESSOR)
        variantHandler.defineDefault(builder)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        variantHandler.saveToNBT(compound)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        variantHandler.loadFromNBT(compound)
    }

    val variant: HumanoidVariant by lazy { variantHandler.getVariant() }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnGroupData: SpawnGroupData?
    ): SpawnGroupData? {
        variantHandler.setRandomVariant()

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData)
    }

    override val xpRewardOnKill: Int = 12
    override fun getNameTag(): Component = Component.translatable(LangKeys.ENTITY_BANDIT_REGULAR)
}
