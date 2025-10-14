package dev.echoellet.dragonfist_legacy.entity.bandit.rank.enforcer

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.level.Level

class BanditEnforcerEntity(
    type: EntityType<BanditEnforcerEntity>,
    world: Level,
) : BanditEntity(type, world) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder = createBaseAttributes(
            maxHealth = 35.0,
            movementSpeed = 0.35,
            stepHeight = 6.0,
            knockbackResistance = 0.3,
            attackDamage = 1.0,
            armor = 5.0,
        )
    }

    override val xpRewardOnKill: Int = 16

    override fun getNameTag(): Component = Component.translatable(LangKeys.ENTITY_BANDIT_ENFORCER)

    override fun getRank(): BanditRank = BanditRank.Enforcer
}
