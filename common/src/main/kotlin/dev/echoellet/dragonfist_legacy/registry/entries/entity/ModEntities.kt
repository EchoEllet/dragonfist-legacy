package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
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
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object ModEntities {
    lateinit var SHIFU: DeferredEntity<ShifuEntity>

    // Bandit entity IDs are defined in build.gradle.kts and must remain in sync with these IDs.
    // Do not change these IDs here without updating the `BanditRank` enum in build.gradle.kts.

    lateinit var BANDIT_REGULAR: DeferredEntity<BanditRegularEntity>
    lateinit var BANDIT_ENFORCER: DeferredEntity<BanditEnforcerEntity>
    lateinit var BANDIT_CHAMPION: DeferredEntity<BanditChampionEntity>
    lateinit var BANDIT_ELITE: DeferredEntity<BanditEliteEntity>
    lateinit var BANDIT_LEADER: DeferredEntity<BanditLeaderEntity>
    lateinit var BANDIT_RULER: DeferredEntity<BanditRulerEntity>

    @Suppress("UNCHECKED_CAST")
    fun getBanditEntityType(banditRank: BanditRank): DeferredEntity<BanditEntity> = when (banditRank) {
        BanditRank.Regular -> BANDIT_REGULAR
        BanditRank.Enforcer -> BANDIT_ENFORCER
        BanditRank.Champion -> BANDIT_CHAMPION
        BanditRank.Elite -> BANDIT_ELITE
        BanditRank.Leader -> BANDIT_LEADER
        BanditRank.Ruler -> BANDIT_RULER
    } as DeferredEntity<BanditEntity>

    lateinit var KNIGHT: DeferredEntity<KnightEntity>

    private fun <T : Entity> registerEntity(
        name: String,
        builder: EntityType.Builder<T>
    ): DeferredEntity<T> {
        return ModPlatformProvider.get().deferredRegistrar().registerEntity(name, builder)
    }

    private const val PLAYER_WIDTH = 0.6f
    private const val PLAYER_HEIGHT = 1.8f

    private fun <T : Entity> EntityType.Builder<T>.playerSized(): EntityType.Builder<T> {
        return this.sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    }

    fun register() {
        SHIFU = registerEntity(
            "shifu",
            EntityType.Builder.of(::ShifuEntity, MobCategory.CREATURE)
                .fireImmune()
                .playerSized()
        )

        BANDIT_REGULAR = registerEntity(
            "bandit_regular",
            EntityType.Builder.of(::BanditRegularEntity, MobCategory.MONSTER)
                .playerSized()
        )
        BANDIT_ENFORCER = registerEntity(
            "bandit_enforcer",
            EntityType.Builder.of(::BanditEnforcerEntity, MobCategory.MONSTER)
                .playerSized()
        )
        BANDIT_CHAMPION = registerEntity(
            "bandit_champion",
            EntityType.Builder.of(::BanditChampionEntity, MobCategory.MONSTER)
                .playerSized()
        )
        BANDIT_ELITE = registerEntity(
            "bandit_elite",
            EntityType.Builder.of(::BanditEliteEntity, MobCategory.MONSTER)
                .playerSized()
        )
        BANDIT_LEADER = registerEntity(
            "bandit_leader",
            EntityType.Builder.of(::BanditLeaderEntity, MobCategory.MONSTER)
                .playerSized()
        )
        BANDIT_RULER = registerEntity(
            "bandit_ruler",
            EntityType.Builder.of(::BanditRulerEntity, MobCategory.MONSTER)
                .playerSized()
        )

        KNIGHT = registerEntity(
            "knight",
            EntityType.Builder.of(::KnightEntity, MobCategory.CREATURE)
                .playerSized()
        )
    }
}
