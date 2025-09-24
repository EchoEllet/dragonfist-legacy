package dev.echoellet.dragonfist_legacy.entity.bandit.rank.ruler

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.BanditBossEventHandler
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.NearbyPlayersMessenger
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.common.CombatMessageSequencer
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.BossEvent
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

class BanditRulerEntity(
    type: EntityType<BanditRulerEntity>,
    world: Level,
) : BanditEntity(type, world) {
    private val bossEventHandler = BanditBossEventHandler(this, BossEvent.BossBarColor.PURPLE) {
        if (gender.isMale) LangKeys.ENTITY_BANDIT_RULER_BOSS_MALE else LangKeys.ENTITY_BANDIT_RULER_BOSS_FEMALE
    }
    private val combatMessageSequencer = CombatMessageSequencer(
        this, listOf(
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_1,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_2,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_3,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_4,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_5,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_6,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_7,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_8,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_9,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_10,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_11,
            LangKeys.ENTITY_BANDIT_RULER_DURING_COMBAT_12,
        )
    )

    override fun readAdditionalSaveData(compound: CompoundTag) {
        super.readAdditionalSaveData(compound)

        // Must be called after super.readAdditionalSaveData() to ensure NBT data is loaded
        bossEventHandler.initializeBossEvent()
    }

    @Deprecated("Override-Only")
    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnType: MobSpawnType,
        spawnGroupData: SpawnGroupData?
    ): SpawnGroupData? {
        @Suppress("DEPRECATION")
        val result = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData)

        // Must be called after super.finalizeSpawn() to ensure NBT data is loaded
        bossEventHandler.initializeBossEvent()
        return result
    }

    override fun startSeenByPlayer(player: ServerPlayer) {
        super.startSeenByPlayer(player)
        bossEventHandler.addPlayer(player)
    }

    override fun stopSeenByPlayer(player: ServerPlayer) {
        super.stopSeenByPlayer(player)
        bossEventHandler.removePlayer(player)
    }

    public override fun customServerAiStep() {
        super.customServerAiStep()
        bossEventHandler.updateHealth()
    }

    override fun tick() {
        super.tick()

        if (!level().isClientSide) {
            combatMessageSequencer.tick()
        }
    }

    override fun die(damageSource: DamageSource) {
        super.die(damageSource)

        if (!level().isClientSide) {
            NearbyPlayersMessenger.sendMessage(LangKeys.ENTITY_BANDIT_RULER_DEATH, this)
        }
    }

    companion object {
        // TODO: Vanilla and Epic Fight Attributes are not balanced at all for all bandit entities/ranks!
        fun createAttributes(): AttributeSupplier.Builder = createBaseAttributes(
            maxHealth = 200.0,
            movementSpeed = 0.5,
            stepHeight = 16.0,
            knockbackResistance = 0.6,
            attackDamage = 6.0,
            armor = 60.0,
        )
    }

    override val xpRewardOnKill: Int = 100

    override fun getDisplayName(): Component = Component.translatable(LangKeys.ENTITY_BANDIT_RULER)

    override fun getRank(): BanditRank = BanditRank.Ruler
}
