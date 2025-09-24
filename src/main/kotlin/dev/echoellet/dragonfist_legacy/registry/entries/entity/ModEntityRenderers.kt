package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.champion.BanditChampionRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.elite.BanditEliteRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.enforcer.BanditEnforcerRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader.BanditLeaderRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.regular.BanditRegularRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.ruler.BanditRulerRenderer
import dev.echoellet.dragonfist_legacy.entity.knight.KnightRenderer
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuRenderer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent

@EventBusSubscriber(
    modid = DragonFistLegacy.ID,
    value = [Dist.CLIENT]
)
@Suppress("unused")
object ModEntityRenderers {

    @SubscribeEvent
    fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(ModEntities.SHIFU.get()) { context -> ShifuRenderer(context) }
        event.registerEntityRenderer(ModEntities.KNIGHT.get()) { context -> KnightRenderer(context) }

        registerBandits(event)
    }

    private fun registerBandits(event: EntityRenderersEvent.RegisterRenderers) {
        for (rank in BanditRank.entries) {
            when (rank) {
                BanditRank.Regular -> event.registerEntityRenderer(
                    ModEntities.BANDIT_REGULAR.get(),
                    ::BanditRegularRenderer,
                )

                BanditRank.Enforcer -> event.registerEntityRenderer(
                    ModEntities.BANDIT_ENFORCER.get(),
                    ::BanditEnforcerRenderer,
                )

                BanditRank.Champion -> event.registerEntityRenderer(
                    ModEntities.BANDIT_CHAMPION.get(),
                    ::BanditChampionRenderer,
                )

                BanditRank.Elite -> event.registerEntityRenderer(
                    ModEntities.BANDIT_ELITE.get(),
                    ::BanditEliteRenderer,
                )

                BanditRank.Leader -> event.registerEntityRenderer(
                    ModEntities.BANDIT_LEADER.get(),
                    ::BanditLeaderRenderer,
                )

                BanditRank.Ruler -> event.registerEntityRenderer(
                    ModEntities.BANDIT_RULER.get(),
                    ::BanditRulerRenderer,
                )
            }
        }
    }
}
