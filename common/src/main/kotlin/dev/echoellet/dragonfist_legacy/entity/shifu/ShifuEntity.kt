package dev.echoellet.dragonfist_legacy.entity.shifu

import dev.echoellet.dragonfist_legacy.api.event.ModEvents
import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader.BanditLeaderEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.ruler.BanditRulerEntity
import dev.echoellet.dragonfist_legacy.entity.common.EntityPassiveHpRegenManager
import dev.echoellet.dragonfist_legacy.entity.common.MobFocusManager
import dev.echoellet.dragonfist_legacy.entity.common.MobHomeTracker
import dev.echoellet.dragonfist_legacy.entity.common.goals.GoBackToHomeGoal
import dev.echoellet.dragonfist_legacy.entity.shifu.combat.ShifuMercifulHurtByTargetGoal
import dev.echoellet.dragonfist_legacy.entity.shifu.combat.ShifuPlayerTrainingIntroManager
import dev.echoellet.dragonfist_legacy.entity.shifu.combat.ShifuPlayerTrainingPolicy
import dev.echoellet.dragonfist_legacy.entity.shifu.handlers.ShifuDeathHandler
import dev.echoellet.dragonfist_legacy.entity.shifu.handlers.ShifuInteractionHandler
import dev.echoellet.dragonfist_legacy.entity.shifu.handlers.ShifuRainReactionManager
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageKeys
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuSoundEvents
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredAttributeSupplier
import dev.echoellet.dragonfist_legacy.registry.entries.item.ModItems
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerBossEvent
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.BossEvent
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.FloatGoal
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal
import net.minecraft.world.entity.ai.goal.OpenDoorGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.ai.goal.RandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.boss.enderdragon.EnderDragon
import net.minecraft.world.entity.boss.wither.WitherBoss
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.npc.AbstractVillager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

// TODO: Modify Epic Fight mob patch to make Shifu stronger (too weak right now)

class ShifuEntity(
    type: EntityType<ShifuEntity>,
    world: Level
) : PathfinderMob(type, world) {
    companion object {
        fun createAttributes(): DeferredAttributeSupplier {
            return DeferredAttributeSupplier {
                createMobAttributes().apply {
                    add(Attributes.MOVEMENT_SPEED, 0.4)
                    add(Attributes.WATER_MOVEMENT_EFFICIENCY, 0.05)
                    add(Attributes.ATTACK_SPEED, 1.2)
                    add(Attributes.MAX_HEALTH, 200.0)
                    add(Attributes.ARMOR, 30.0)
                    add(Attributes.ARMOR_TOUGHNESS, 3.0)
                    add(Attributes.ATTACK_DAMAGE, 6.0)
                    add(Attributes.FOLLOW_RANGE, 32.0)
                    add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                    add(Attributes.ATTACK_KNOCKBACK, 1.0)
                    add(Attributes.STEP_HEIGHT, 6.0)
                }
            }
        }

        private val IGNORED_DAMAGE_TYPES = setOf(
            DamageTypes.IN_FIRE,
            DamageTypes.FALL,
            DamageTypes.CACTUS,
            DamageTypes.DROWN,
            DamageTypes.LIGHTNING_BOLT,
            DamageTypes.EXPLOSION,
            DamageTypes.PLAYER_EXPLOSION,
            DamageTypes.FALLING_ANVIL,
        )

        private const val XP_REWARD = 100
    }

    private val focusManager = MobFocusManager(this)
    private val interactionHandler = ShifuInteractionHandler(this)
    private val rainReactionManager = ShifuRainReactionManager(this)
    private val passiveHpRegenManager = EntityPassiveHpRegenManager(this, isInCombat = ::isInCombat)
    private val playerTrainingPolicy = ShifuPlayerTrainingPolicy(this)
    private val deathHandler = ShifuDeathHandler(this)
    private val platerTrainingIntroManager = ShifuPlayerTrainingIntroManager(serverBossEvent = { bossEvent })
    private val homeTracker = MobHomeTracker(this, storeOriginalSpawnPos = true)

    private var bossEvent: ServerBossEvent = ServerBossEvent(
        Component.translatable(LangKeys.ENTITY_SHIFU_BOSS),
        BossEvent.BossBarColor.GREEN,
        BossEvent.BossBarOverlay.PROGRESS,
    )

    init {
        xpReward = XP_REWARD
        isNoAi = false
        playerLikeName()
        setPersistenceRequired()
    }

    private fun playerLikeName() {
        customName = getNameTag() // Player like name
        isCustomNameVisible = true
    }

    private fun getNameTag(): Component = Component.translatable(LangKeys.ENTITY_SHIFU)

    override fun removeWhenFarAway(distanceToClosestPlayer: Double): Boolean = false

    /**
     * [registerGoals] called in the constructor of [net.minecraft.world.entity.Mob], so all
     * properties created at the class-level will be null inside this method.
     */
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
            MoveBackToVillageGoal(this, 0.6, false),
            GoBackToHomeGoal(
                mob = this,
                nearHomeDistance = 16.0,
                speed = 0.6,
                homePos = { homeTracker.homePos },
                shouldGoHome = {
                    val level = level()
                    val isNight = level.isNight || level.dayTime % 24000L >= 12000L - 2000L
                    isNight
                },
            ),
            LookAtPlayerGoal(this, Player::class.java, 10.0f),
            LookAtPlayerGoal(this, AbstractVillager::class.java, 4.0f),
            OpenDoorGoal(this, true),
            RandomStrollGoal(this, 0.5),
            FloatGoal(this),
            RandomLookAroundGoal(this)
        )

        for ((index, goal) in goals.withIndex()) {
            val priority = index + 1

            this.goalSelector.addGoal(priority, goal)
        }
    }

    private fun registerTargetGoals() {
        val targetGoals: List<TargetGoal> = listOf(
            NearestAttackableTargetGoal(this, BanditRulerEntity::class.java, true, true),
            NearestAttackableTargetGoal(this, BanditLeaderEntity::class.java, true, true),
            NearestAttackableTargetGoal(this, BanditEntity::class.java, true, true),
            NearestAttackableTargetGoal(this, Monster::class.java, true, true),
            ShifuMercifulHurtByTargetGoal(this) {
                /**
                 * IMPORTANT: [registerGoals] is called in the constructor of [net.minecraft.world.entity.Mob].
                 * At that point, class-level properties like [playerTrainingPolicy] are not fully initialized.
                 * Directly passing the property would throw a [NullPointerException].
                 * Using a provider/lambda ensures the policy is accessed lazily, only when the goal actually runs.
                 */
                playerTrainingPolicy
            },
            NearestAttackableTargetGoal(this, EnderDragon::class.java, true, false),
            NearestAttackableTargetGoal(this, WitherBoss::class.java, true, false),
        )

        for ((index, targetGoal) in targetGoals.withIndex()) {
            val priority = index + 1

            this.targetSelector.addGoal(priority, targetGoal)
        }
    }

    fun isInCombat(): Boolean = target?.isAlive == true

    override fun getAmbientSound(): SoundEvent? {
        if (isInCombat()) {
            // Silent and focused during combat
            return null
        }
        return ShifuSoundEvents.getAmbientSound()
    }

    override fun getHurtSound(ds: DamageSource): SoundEvent = ShifuSoundEvents.getHurtSound()

    override fun getDeathSound(): SoundEvent = ShifuSoundEvents.getDeathSound()

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        return interactionHandler.handle(player, hand)
    }

    override fun tick() {
        super.tick()

        if (!level().isClientSide) {
            focusManager.updateFocus()
            rainReactionManager.tick()
            passiveHpRegenManager.tick()
        }
    }

    fun focusOnPlayer(player: Player, durationSeconds: Int) = focusManager.focusOnEntity(player, durationSeconds)

    override fun awardKillScore(killed: Entity, scoreValue: Int, source: DamageSource) {
        super.awardKillScore(killed, scoreValue, source)
        if (killed is Player) {
            ShifuPlayerTrainingPolicy.handlePlayerDeathDuringTraining(killed)
        }
    }

    override fun setTarget(newTarget: LivingEntity?) {
        val previousTarget = this.target
        super.setTarget(newTarget)

        platerTrainingIntroManager.handleTargetChange(previousTarget, newTarget)
    }

    override fun hurt(source: DamageSource, amount: Float): Boolean {
        if (IGNORED_DAMAGE_TYPES.any(source::`is`)) {
            return false
        }

        val sourceEntity = source.entity

        if (sourceEntity is Player && !sourceEntity.isCreative) {
            val canPlayerHurtShifu = playerTrainingPolicy.handleStartTrainingAttempt(sourceEntity)
            if (!canPlayerHurtShifu) {
                return false
            }

            platerTrainingIntroManager.notifyPlayerHit(sourceEntity)
        }

        // Clear focus target whenever Shifu is attacked
        focusManager.clearFocus()
        passiveHpRegenManager.onHurt()

        return super.hurt(source, amount)
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnGroupData: SpawnGroupData?
    ): SpawnGroupData? {
        homeTracker.setCurrentPosition()
        ModEvents.Shifu.ON_FINALIZE_SPAWN.invoke(ModEvents.FinalizeSpawn(this))

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData)
    }

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)
        homeTracker.loadNbt(compound)
    }

    override fun addAdditionalSaveData(compound: CompoundTag) {
        super.addAdditionalSaveData(compound)
        homeTracker.saveNbt(compound)
    }

    override fun ignoreExplosion(explosion: Explosion): Boolean = true

    override fun fireImmune(): Boolean = true

    override fun die(source: DamageSource) {
        super.die(source)

        deathHandler.handle(
            sourceEntity = source.entity,
            killerMessageKey = ShifuMessageKeys.SHIFU_KILLED_BY_PLAYER,
            naturalMessageKey = ShifuMessageKeys.SHIFU_KILLED_NATURALLY,
        )
    }

    override fun dropCustomDeathLoot(level: ServerLevel, damageSource: DamageSource, recentlyHit: Boolean) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit)

        spawnAtLocation(ItemStack(Items.DIAMOND))
        spawnAtLocation(ItemStack(ModItems.UNCOMMON_SCROLL.get()))
    }

    override fun stopSeenByPlayer(player: ServerPlayer) {
        super.stopSeenByPlayer(player)
        this.bossEvent.removePlayer(player)
    }

    public override fun customServerAiStep() {
        super.customServerAiStep()
        this.bossEvent.setProgress(this.health / this.maxHealth)
    }
}
