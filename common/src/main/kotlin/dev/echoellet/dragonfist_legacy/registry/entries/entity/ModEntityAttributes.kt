package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.champion.BanditChampionEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.elite.BanditEliteEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.enforcer.BanditEnforcerEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader.BanditLeaderEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.regular.BanditRegularEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.ruler.BanditRulerEntity
import dev.echoellet.dragonfist_legacy.entity.knight.KnightEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.platform.ModPlatformProvider
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredAttributeSupplier
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import net.minecraft.world.entity.LivingEntity

object ModEntityAttributes {
    fun register() {
        registerAttribute(
            ModEntities.SHIFU,
            ShifuEntity.createAttributes(),
        )
        registerAttribute(
            ModEntities.KNIGHT,
            KnightEntity.createAttributes(),
        )

        registerBandits()
    }

    private fun registerBandits() {
        for (rank in BanditRank.entries) {
            val attributes = when (rank) {
                BanditRank.Regular -> BanditRegularEntity.createAttributes()
                BanditRank.Enforcer -> BanditEnforcerEntity.createAttributes()
                BanditRank.Champion -> BanditChampionEntity.createAttributes()
                BanditRank.Elite -> BanditEliteEntity.createAttributes()
                BanditRank.Leader -> BanditLeaderEntity.createAttributes()
                BanditRank.Ruler -> BanditRulerEntity.createAttributes()
            }

            val entityType = ModEntities.getBanditEntityType(rank)
            registerAttribute(
                entityType,
                attributes,
            )
        }
    }

    private fun <T : LivingEntity> registerAttribute(
        entityType: DeferredEntity<T>,
        attributes: DeferredAttributeSupplier,
    ) {
        return ModPlatformProvider.get().entityAttributeRegistrar().register(entityType, attributes)
    }
}
