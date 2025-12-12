package dev.echoellet.dragonfist_legacy.client.entity

import dev.echoellet.dragonfist_legacy.client.platform.ClientModPlatformProvider
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.champion.BanditChampionRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.elite.BanditEliteRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.enforcer.BanditEnforcerRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader.BanditLeaderRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.regular.BanditRegularRenderer
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.ruler.BanditRulerRenderer
import dev.echoellet.dragonfist_legacy.entity.knight.KnightRenderer
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuRenderer
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity

object ModEntityRenderers {
    fun register() {
        registerRenderer(ModEntities.SHIFU) { context -> ShifuRenderer(context) }
        registerRenderer(ModEntities.KNIGHT) { context -> KnightRenderer(context) }
        registerBandits()
    }

    private fun registerBandits() {
        for (rank in BanditRank.entries) {
            when (rank) {
                BanditRank.Regular -> registerRenderer(
                    ModEntities.BANDIT_REGULAR,
                    ::BanditRegularRenderer,
                )

                BanditRank.Enforcer -> registerRenderer(
                    ModEntities.BANDIT_ENFORCER,
                    ::BanditEnforcerRenderer,
                )

                BanditRank.Champion -> registerRenderer(
                    ModEntities.BANDIT_CHAMPION,
                    ::BanditChampionRenderer,
                )

                BanditRank.Elite -> registerRenderer(
                    ModEntities.BANDIT_ELITE,
                    ::BanditEliteRenderer,
                )

                BanditRank.Leader -> registerRenderer(
                    ModEntities.BANDIT_LEADER,
                    ::BanditLeaderRenderer,
                )

                BanditRank.Ruler -> registerRenderer(
                    ModEntities.BANDIT_RULER,
                    ::BanditRulerRenderer,
                )
            }
        }
    }

    private fun <T : Entity> registerRenderer(
        entityType: DeferredEntity<T>,
        renderer: EntityRendererProvider<T>
    ) {
        return ClientModPlatformProvider.get().entityRendererRegistrar().register(entityType, renderer)
    }
}
