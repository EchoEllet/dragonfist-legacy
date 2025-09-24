package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
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
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEntities {
    val REGISTRY: DeferredRegister<EntityType<*>> = DeferredRegister.create(
        BuiltInRegistries.ENTITY_TYPE, DragonFistLegacy.ID
    )

    private const val PLAYER_WIDTH = 0.6f
    private const val PLAYER_HEIGHT = 1.8f

    val SHIFU: Supplier<EntityType<ShifuEntity>> = registerEntity(
        "shifu",
        EntityType.Builder.of(::ShifuEntity, MobCategory.CREATURE)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(64)
            .setUpdateInterval(3)
            .fireImmune()
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    // Bandit entity IDs are defined in build.gradle.kts and must remain in sync with these IDs.
    // Do not change these IDs here without updating the `BanditRank` enum in build.gradle.kts.

    val BANDIT_REGULAR: Supplier<EntityType<BanditRegularEntity>> = registerEntity(
        "bandit_regular",
        EntityType.Builder.of(::BanditRegularEntity, MobCategory.MONSTER)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(32)
            .setUpdateInterval(3)
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    val BANDIT_ENFORCER: Supplier<EntityType<BanditEnforcerEntity>> = registerEntity(
        "bandit_enforcer",
        EntityType.Builder.of(::BanditEnforcerEntity, MobCategory.MONSTER)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(32)
            .setUpdateInterval(3)
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    val BANDIT_CHAMPION: Supplier<EntityType<BanditChampionEntity>> = registerEntity(
        "bandit_champion",
        EntityType.Builder.of(::BanditChampionEntity, MobCategory.MONSTER)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(32)
            .setUpdateInterval(3)
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    val BANDIT_ELITE: Supplier<EntityType<BanditEliteEntity>> = registerEntity(
        "bandit_elite",
        EntityType.Builder.of(::BanditEliteEntity, MobCategory.MONSTER)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(32)
            .setUpdateInterval(3)
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    val BANDIT_LEADER: Supplier<EntityType<BanditLeaderEntity>> = registerEntity(
        "bandit_leader",
        EntityType.Builder.of(::BanditLeaderEntity, MobCategory.MONSTER)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(32)
            .setUpdateInterval(3)
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    val BANDIT_RULER: Supplier<EntityType<BanditRulerEntity>> = registerEntity(
        "bandit_ruler",
        EntityType.Builder.of(::BanditRulerEntity, MobCategory.MONSTER)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(32)
            .setUpdateInterval(3)
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    fun getBanditEntityType(banditRank: BanditRank): Supplier<out EntityType<out BanditEntity>> = when (banditRank) {
        BanditRank.Regular -> BANDIT_REGULAR
        BanditRank.Enforcer -> BANDIT_ENFORCER
        BanditRank.Champion -> BANDIT_CHAMPION
        BanditRank.Elite -> BANDIT_ELITE
        BanditRank.Leader -> BANDIT_LEADER
        BanditRank.Ruler -> BANDIT_RULER
    }

    val KNIGHT: Supplier<EntityType<KnightEntity>> = registerEntity(
        "knight",
        EntityType.Builder.of(::KnightEntity, MobCategory.CREATURE)
            .setShouldReceiveVelocityUpdates(true)
            .setTrackingRange(50)
            .setUpdateInterval(3)
            .sized(PLAYER_WIDTH, PLAYER_HEIGHT)
    )

    private fun <T : Entity> registerEntity(
        name: String,
        builder: EntityType.Builder<T>
    ): Supplier<EntityType<T>> {
        return REGISTRY.register(name, Supplier { builder.build(name) })
    }
}
