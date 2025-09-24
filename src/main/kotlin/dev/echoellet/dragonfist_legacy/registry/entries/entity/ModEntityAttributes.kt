package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.champion.BanditChampionEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.elite.BanditEliteEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.enforcer.BanditEnforcerEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader.BanditLeaderEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.regular.BanditRegularEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.ruler.BanditRulerEntity
import dev.echoellet.dragonfist_legacy.entity.knight.KnightEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent

@EventBusSubscriber(modid = DragonFistLegacy.ID)
@Suppress("unused")
object ModEntityAttributes {

    @SubscribeEvent
    fun onEntityAttributeCreation(event: EntityAttributeCreationEvent) {
        event.put(
            ModEntities.SHIFU.get(),
            ShifuEntity.createAttributes().build(),
        )
        event.put(
            ModEntities.KNIGHT.get(),
            KnightEntity.createAttributes().build(),
        )

        registerBandits(event)
    }

    private fun registerBandits(event: EntityAttributeCreationEvent) {
        for (rank in BanditRank.entries) {
            val attributes = when (rank) {
                BanditRank.Regular -> BanditRegularEntity.createAttributes()
                BanditRank.Enforcer -> BanditEnforcerEntity.createAttributes()
                BanditRank.Champion -> BanditChampionEntity.createAttributes()
                BanditRank.Elite -> BanditEliteEntity.createAttributes()
                BanditRank.Leader -> BanditLeaderEntity.createAttributes()
                BanditRank.Ruler -> BanditRulerEntity.createAttributes()
            }.build()

            event.put(
                ModEntities.getBanditEntityType(rank).get(),
                attributes,
            )
        }
    }
}
