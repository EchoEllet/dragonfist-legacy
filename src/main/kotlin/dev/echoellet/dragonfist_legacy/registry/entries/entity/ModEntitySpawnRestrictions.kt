package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.SpawnPlacementTypes
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.levelgen.Heightmap
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent
import java.util.function.Supplier

@EventBusSubscriber(modid = DragonFistLegacy.ID)
object ModEntitySpawnRestrictions {
    @SubscribeEvent
    fun onRegisterRestrictions(event: RegisterSpawnPlacementsEvent) {
        registerBandits(event)
    }

    private fun registerBandits(event: RegisterSpawnPlacementsEvent) {
        fun <T : BanditEntity> registerBandit(entityType: Supplier<EntityType<T>>) {
            event.register(
                entityType.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                { entityType, level, spawnType, pos, random ->
                    level.difficulty != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(
                        level,
                        pos,
                        random
                    ) && Mob.checkMobSpawnRules(entityType, level, spawnType, pos, random)
                },
                RegisterSpawnPlacementsEvent.Operation.AND,
            )
        }

        registerBandit(ModEntities.BANDIT_REGULAR)
        registerBandit(ModEntities.BANDIT_ENFORCER)
    }
}