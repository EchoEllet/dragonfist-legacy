package dev.echoellet.dragonfist_legacy.entity.bandit.rank.champion

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class BanditChampionEntity(
    type: EntityType<BanditChampionEntity>,
    world: Level,
) : BanditEntity(type, world) {
    companion object {
        fun createAttributes() = createBaseAttributes(
            maxHealth = 60.0,
            movementSpeed = 0.4,
            stepHeight = 8.0,
            knockbackResistance = 0.4,
            attackDamage = 2.0,
            armor = 15.0,
        )
    }

    override val xpRewardOnKill: Int = 25

    override fun getNameTag(): Component = Component.translatable(LangKeys.ENTITY_BANDIT_CHAMPION)

    override fun getRank(): BanditRank = BanditRank.Champion
}
