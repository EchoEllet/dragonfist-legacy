package dev.echoellet.dragonfist_legacy.entity.bandit

import dev.echoellet.dragonfist_legacy.entity.bandit.handler.BanditArmorEquipper
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.BanditLootDropper
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.BanditScrollDropper
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.BanditWeaponEquipper
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.bandit.util.BanditSound
import dev.echoellet.dragonfist_legacy.entity.bandit.util.BanditSoundEvents
import dev.echoellet.dragonfist_legacy.entity.common.EntityPassiveHpRegenManager
import dev.echoellet.dragonfist_legacy.entity.common.MobSpawnTypeManager
import dev.echoellet.dragonfist_legacy.entity.common.gender.EntityGenderHandler
import dev.echoellet.dragonfist_legacy.entity.common.gender.Gender
import dev.echoellet.dragonfist_legacy.entity.knight.KnightEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredAttributeSupplier
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.BreakDoorGoal
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.FollowMobGoal
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.OpenDoorGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.npc.WanderingTrader
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

abstract class BanditEntity(
    type: EntityType<out BanditEntity>,
    world: Level,
) : Monster(type, world) {
    companion object {
        private val GENDER_ACCESSOR: EntityDataAccessor<Int> =
            SynchedEntityData.defineId(BanditEntity::class.java, EntityDataSerializers.INT)

        fun createBaseAttributes(
            maxHealth: Double,
            movementSpeed: Double,
            followRange: Double = 32.0,
            stepHeight: Double,
            knockbackResistance: Double? = null,
            attackDamage: Double? = null,
            attackSpeed: Double? = null,
            armor: Double? = null,
            armorToughness: Double? = null,
        ): DeferredAttributeSupplier = DeferredAttributeSupplier {
            createMonsterAttributes().apply {
                add(Attributes.MAX_HEALTH, maxHealth)
                add(Attributes.MOVEMENT_SPEED, movementSpeed)
                add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.05)
                add(Attributes.FOLLOW_RANGE, followRange)
                add(Attributes.STEP_HEIGHT, stepHeight)
                knockbackResistance?.let { add(Attributes.KNOCKBACK_RESISTANCE, knockbackResistance) }
                attackDamage?.let { add(Attributes.ATTACK_DAMAGE, attackDamage) }
                attackSpeed?.let { add(Attributes.ATTACK_SPEED, attackSpeed) }
                armorToughness?.let { add(Attributes.ARMOR_TOUGHNESS, armorToughness) }
                armor?.let { add(Attributes.ARMOR, armor) }
            }
        }

        private const val KILL_HEAL_PERCENT = 0.05f // 5% of max health
    }

    abstract val xpRewardOnKill: Int

    /**
     * IMPORTANT: This must be created in [defineSynchedData]. For more details, see [EntityGenderHandler].
     */
    private lateinit var genderHandler: EntityGenderHandler

    /**
     * IMPORTANT: This must be created in [defineSynchedData] (similar to [EntityGenderHandler]).
     */
    private lateinit var spawnTypeManager: MobSpawnTypeManager

    private val passiveHpRegenManager = EntityPassiveHpRegenManager(this, isInCombat = ::isInCombat)
    private val armorEquipper = BanditArmorEquipper(this)
    private val weaponEquipper = BanditWeaponEquipper(this)
    private val scrollDropper = BanditScrollDropper(this)
    private val lootDropper = BanditLootDropper(this)

    private fun isInCombat(): Boolean = target?.isAlive == true

    init {
        xpReward = xpRewardOnKill
        isNoAi = false

        playerLikeName()
    }

    private fun playerLikeName() {
        customName = getNameTag()
        isCustomNameVisible = false
    }

    abstract fun getNameTag(): Component

    /**
     * Returns the rank of this BanditEntity.
     *
     * IMPORTANT:
     * - MUST be implemented as a function or a getter.
     * - MUST NOT be a class property with a backing field.
     * - This function is accessed during [registerGoals] of [net.minecraft.world.entity.Mob],
     *   which is called in the constructor of [net.minecraft.world.entity.Mob].
     *   If implemented as a normal property with a backing field, the property
     *   will be null at that point, causing a runtime [NullPointerException] (this is the issue
     *   that was reproduced).
     *
     * NOTE for project maintainers: This class must not know anything about the enums in the [BanditRank],
     * although it's allowed to access [BanditRank.isCommon].
     */
    abstract fun getRank(): BanditRank

    private fun isSpawnType(type: MobSpawnType): Boolean {
        val spawnType = requireNotNull(mobSpawnType) { "Failed to compare the MobSpawnType as it's null" }
        return spawnType == type
    }

    private fun spawnedInStructure(): Boolean = isSpawnType(MobSpawnType.STRUCTURE)

    private fun spawnedNaturally(): Boolean = isSpawnType(MobSpawnType.NATURAL)

    // Only persist common bandits that were not spawned naturally in the world (i.e., spawned inside a structure).
    private fun isPersistentEntity(): Boolean {
        requireNotNull(!level().isClientSide()) { "Must be called only on the server side" }
        return (!getRank().isCommon || spawnedInStructure())
    }

    override fun isPersistenceRequired(): Boolean {
        return isPersistentEntity()
    }

    private fun mustBanditSeePlayerToTarget(): Boolean = getRank().isCommon && spawnedNaturally()

    private fun canRoamFreely(): Boolean = !spawnedInStructure()

    override fun removeWhenFarAway(distanceToClosestPlayer: Double): Boolean = !isPersistentEntity()

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
            object : FollowMobGoal(this, 1.0, 10.0f, 5.0f) {
                override fun canUse(): Boolean {
                    return super.canUse() && canRoamFreely()
                }
            },
            object : WaterAvoidingRandomStrollGoal(this, 0.6) {
                override fun canUse(): Boolean {
                    return super.canUse() && canRoamFreely()
                }
            },
            RandomLookAroundGoal(this),
            FloatGoal(this),
            BreakDoorGoal(this) { _ -> true },
            OpenDoorGoal(this, false),
        )

        for ((index, goal) in goals.withIndex()) {
            val priority = index + 1
            this.goalSelector.addGoal(priority, goal)
        }
    }

    private fun registerTargetGoals() {
        val targetGoals: List<TargetGoal> = listOf(
            HurtByTargetGoal(this),
            NearestAttackableTargetGoal(
                this, Player::class.java,
                // TODO: Since registerGoals() is called in the constructor, spawnType is null at this point,
                //  and therefore mustBanditSeePlayerToTarget() will throw a runtime error.
                //  To save development time, this has been temporarily changed from "mustBanditSeePlayerToTarget()" to "false".
                //  Later, it must be updated to use mustBanditSeePlayerToTarget()
                false,
                false
            ),
            NearestAttackableTargetGoal(this, KnightEntity::class.java, false, true),
            NearestAttackableTargetGoal(this, ShifuEntity::class.java, true, true),
            NearestAttackableTargetGoal(this, IronGolem::class.java, false, true),
            NearestAttackableTargetGoal(this, Villager::class.java, true, true),
            NearestAttackableTargetGoal(this, WanderingTrader::class.java, true, true),
        )

        for ((index, goal) in targetGoals.withIndex()) {
            val priority = index + 1
            this.targetSelector.addGoal(priority, goal)
        }
    }

    override fun defineSynchedData(builder: SynchedEntityData.Builder) {
        super.defineSynchedData(builder)
        genderHandler = EntityGenderHandler(this, GENDER_ACCESSOR)
        genderHandler.defineDefault(builder)

        spawnTypeManager = MobSpawnTypeManager(this)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        genderHandler.saveToNBT(compound)
        spawnTypeManager.saveNbt(compound)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        genderHandler.loadFromNBT(compound)
        spawnTypeManager.loadNbt(compound)
    }

    val gender: Gender by lazy { genderHandler.getGender() }

    // Avoid calling this "spawnType" to avoid conflicts with Forge-like platforms,
    // as they patch vanilla classes directly (i.e., they already add "spawnType").
    private val mobSpawnType: MobSpawnType?
        get() = spawnTypeManager.getMobSpawnType()

    /**
     * IMPORTANT for project maintainers: Keep this lazy. Otherwise, [gender] will always be the default,
     * because it will access the [gender] property directly. At that point, the gender is still [Gender.DEFAULT]
     * and has not yet been loaded (i.e., [readAdditionalSaveData] has not been called).
     */
    private val sounds: BanditSound by lazy { BanditSoundEvents.getByGender(gender) }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnGroupData: SpawnGroupData?
    ): SpawnGroupData? {
        genderHandler.setRandomGender()
        armorEquipper.mayEquipRandomArmorSet()
        weaponEquipper.equipRandomWeapon()
        spawnTypeManager.onFinalizeSpawn(spawnType)

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData)
    }

    override fun tick() {
        super.tick()
        if (!level().isClientSide) {
            passiveHpRegenManager.tick()
        }
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        passiveHpRegenManager.onHurt()
        return super.hurt(source, amount)
    }

    override fun dropCustomDeathLoot(level: ServerLevel, damageSource: DamageSource, recentlyHit: Boolean) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit)

        scrollDropper.chanceToDropScrolls()
        lootDropper.chanceToDropLoot()
    }

    override fun getAmbientSound(): SoundEvent {
        return sounds.getAmbientSound()
    }

    override fun getDeathSound(): SoundEvent = sounds.getDeathSound()

    override fun getHurtSound(damageSource: DamageSource): SoundEvent = sounds.getHurtSound()

    override fun awardKillScore(killed: Entity, scoreValue: Int, source: DamageSource) {
        super.awardKillScore(killed, scoreValue, source)
        healOnKill()

        if (killed is Player && !killed.level().isClientSide) {
            killed.displayClientMessage(getKillPlayerMessage(), false)
        }
    }

    private fun getKillPlayerMessage(): Component {
        val messageKey = when (getRank()) {
            BanditRank.Regular -> LangKeys.ENTITY_BANDIT_REGULAR_KILL_PLAYER
            BanditRank.Enforcer -> LangKeys.ENTITY_BANDIT_ENFORCER_KILL_PLAYER
            BanditRank.Champion -> LangKeys.ENTITY_BANDIT_CHAMPION_KILL_PLAYER
            BanditRank.Elite -> LangKeys.ENTITY_BANDIT_ELITE_KILL_PLAYER
            BanditRank.Leader -> LangKeys.ENTITY_BANDIT_LEADER_KILL_PLAYER
            BanditRank.Ruler -> LangKeys.ENTITY_BANDIT_RULER_KILL_PLAYER
        }
        return Component.translatable(messageKey)
    }

    private fun healOnKill() {
        val healAmount = (this.maxHealth * KILL_HEAL_PERCENT).coerceAtLeast(1.0f) // at least 1 HP
        this.heal(healAmount)
    }
}
