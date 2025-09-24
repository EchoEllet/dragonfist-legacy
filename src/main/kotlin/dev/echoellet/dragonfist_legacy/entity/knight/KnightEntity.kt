package dev.echoellet.dragonfist_legacy.entity.knight

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.common.EntityPassiveHpRegenManager
import dev.echoellet.dragonfist_legacy.entity.common.gender.EntityGenderHandler
import dev.echoellet.dragonfist_legacy.entity.common.gender.Gender
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.util.isVillagerEntity
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.*
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.fluids.FluidType

class KnightEntity(
    type: EntityType<KnightEntity>,
    world: Level,
) : PathfinderMob(type, world) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder = createMobAttributes().apply {
            add(Attributes.MOVEMENT_SPEED, 0.3)
            add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.05)
            add(Attributes.ATTACK_SPEED, 1.2)
            add(Attributes.MAX_HEALTH, 40.0)
            add(Attributes.ARMOR, 20.0)
            add(Attributes.ARMOR_TOUGHNESS, 2.0)
            add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
            add(Attributes.ATTACK_DAMAGE, 4.0)
            add(Attributes.FOLLOW_RANGE, 50.0)
            add(Attributes.ATTACK_KNOCKBACK, 1.0)
            add(Attributes.STEP_HEIGHT, 6.0)
        }

        private val GENDER_ACCESSOR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(KnightEntity::class.java, EntityDataSerializers.INT)
    }

    val passiveHpRegenManager = EntityPassiveHpRegenManager(this, ::isInCombat)

    /**
     * IMPORTANT: This must be created in [defineSynchedData]. For more details, see [EntityGenderHandler].
     */
    private lateinit var genderHandler: EntityGenderHandler
    val gender: Gender by lazy { genderHandler.getGender() }

    val weaponEquipper = KnightWeaponEquipper(this)

    private fun isInCombat(): Boolean = target?.isAlive == true

    init {
        playerLikeName()
        setPersistenceRequired()
    }

    override fun removeWhenFarAway(distanceToClosestPlayer: Double): Boolean = false

    override fun getDisplayName(): Component = Component.translatable(LangKeys.ENTITY_KNIGHT)

    override fun getHurtSound(damageSource: DamageSource): SoundEvent = KnightSoundEvents.getHurtSound()

    override fun getDeathSound(): SoundEvent = KnightSoundEvents.getDeathSound()

    override fun playStepSound(pos: BlockPos, block: BlockState) {
        this.playSound(KnightSoundEvents.getStepSound(), 1.0f, 1.0f)
    }

    private fun playerLikeName() {
        customName = getDisplayName()
        isCustomNameVisible = false
    }

    override fun registerGoals() {
        super.registerGoals()

        this.navigation.nodeEvaluator.setCanOpenDoors(true)
        this.navigation.nodeEvaluator.setCanFloat(true)

        registerNormalGoals()
        registerTargetGoals()
    }

    private fun registerNormalGoals() {
        val goals: List<Goal> = listOf(
            MeleeAttackGoal(this, 1.2, true),
            MoveTowardsTargetGoal(this, 1.2, 48.0F),
            RunBackToVillageGoal(this, 1.0),
            RandomStrollGoal(this, 0.6),
            FloatGoal(this),
            OpenDoorGoal(this, true),
            LookAtPlayerGoal(this, Player::class.java, 10.0F),
            LookAtPlayerGoal(this, Villager::class.java, 4.0F),
            RandomLookAroundGoal(this),
        )

        for ((index, goal) in goals.withIndex()) {
            val priority = index + 1
            this.goalSelector.addGoal(priority, goal)
        }
    }

    private fun registerTargetGoals() {
        val targetGoals: List<TargetGoal> = listOf(
            object : HurtByTargetGoal(
                this,
                Villager::class.java,
                Player::class.java,
                ShifuEntity::class.java,
                IronGolem ::class.java,
            ) {
                override fun canUse(): Boolean {
                    val entity = lastHurtByMob
                    if (entity != null && entity.isVillagerEntity()) {
                        return false
                    }
                    return super.canUse()
                }
            },
            NearestAttackableTargetGoal(this, BanditEntity::class.java, false, true),
            NearestAttackableTargetGoal(this, Monster::class.java, false, true),
        )

        for ((index, goal) in targetGoals.withIndex()) {
            val priority = index + 1
            this.targetSelector.addGoal(priority, goal)
        }
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        val sourceEntity = source.entity
        if (sourceEntity is Player && !sourceEntity.isCreative) {
            return false
        }
        passiveHpRegenManager.onHurt()
        return super.hurt(source, amount)
    }

    @Deprecated("Override-Only")
    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnGroupData: SpawnGroupData?
    ): SpawnGroupData? {
        genderHandler.setRandomGender()
        weaponEquipper.equipRandomWeapon()

        @Suppress("DEPRECATION")
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData)
    }

    override fun tick() {
        super.tick()
        if (!level().isClientSide) {
            passiveHpRegenManager.tick()
        }
    }

    override fun doHurtTarget(entity: Entity): Boolean {
        if (entity is Player) {
            return false
        }
        return super.doHurtTarget(entity)
    }

    override fun isPushedByFluid(type: FluidType): Boolean = false

    override fun canDrownInFluidType(type: FluidType): Boolean {
        if (type == Fluids.WATER.fluidType) {
            return false
        }
        return super.canDrownInFluidType(type)
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        genderHandler = EntityGenderHandler(this, GENDER_ACCESSOR)
        genderHandler.defineDefault(builder)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        genderHandler.loadFromNBT(compound)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        genderHandler.saveToNBT(compound)
    }
}
