package dev.echoellet.dragonfist_legacy.entity.bandit.rank.elite

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.level.Level

class BanditEliteEntity(
    type: EntityType<BanditEliteEntity>,
    world: Level,
) : BanditEntity(type, world) {
    companion object {
        fun createAttributes(): AttributeSupplier.Builder = createBaseAttributes(
            maxHealth = 70.0,
            movementSpeed = 0.425,
            stepHeight = 10.0,
            knockbackResistance = 0.5,
            attackDamage = 2.5,
            armor = 20.0,
            armorToughness = 2.0, // Elite bandits use skinned armor rather than a real armor
        )
    }

    override val xpRewardOnKill: Int = 35

    override fun getDisplayName(): Component = Component.translatable(LangKeys.ENTITY_BANDIT_ELITE)

    override fun getRank(): BanditRank = BanditRank.Elite
}
