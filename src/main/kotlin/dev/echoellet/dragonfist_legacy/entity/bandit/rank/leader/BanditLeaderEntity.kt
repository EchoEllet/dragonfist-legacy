package dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.BanditBossEventHandler
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.NearbyPlayersMessenger
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.common.CombatMessageSequencer
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.BossEvent
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.level.Level

class BanditLeaderEntity(
    type: EntityType<BanditLeaderEntity>,
    world: Level,
) : BanditEntity(type, world) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder = createBaseAttributes(
            maxHealth = 120.0,
            movementSpeed = 0.450,
            knockbackResistance = 0.55,
            attackDamage = 3.5,
            armor = 40.0,
        )
    }

    private val bossEventHandler =
        BanditBossEventHandler(this, BossEvent.BossBarColor.RED) { LangKeys.ENTITY_BANDIT_LEADER }
    private val combatMessageSequencer = CombatMessageSequencer(
        this, listOf(
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_1,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_2,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_3,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_4,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_5,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_6,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_7,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_8,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_9,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_10,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_11,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_12,
            LangKeys.ENTITY_BANDIT_LEADER_DURING_COMBAT_13,
        )
    )

    init {
        bossEventHandler.initializeBossEvent()
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
            NearbyPlayersMessenger.sendMessage(LangKeys.ENTITY_BANDIT_LEADER_DEATH, this)
        }
    }

    override val xpRewardOnKill: Int = 60

    override fun getNameTag(): Component = Component.translatable(LangKeys.ENTITY_BANDIT_LEADER)

    override fun getRank(): BanditRank = BanditRank.Leader
}
