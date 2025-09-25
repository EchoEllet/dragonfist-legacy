package dev.echoellet.dragonfist_legacy.registry.entries.entity

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.SpawnPlacements
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.function.Supplier

@Mod.EventBusSubscriber(modid = DragonFistLegacy.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object ModEntitySpawnRestrictions {
    @SubscribeEvent
    fun onRegisterRestrictions(event: SpawnPlacementRegisterEvent) {
        registerBandits(event)
    }

    private fun registerBandits(event: SpawnPlacementRegisterEvent) {
        fun <T : BanditEntity> registerBandit(entityType: Supplier<EntityType<T>>) {
            event.register(
                entityType.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                { entityType, level, spawnType, pos, random ->
                    level.difficulty != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(
                        level,
                        pos,
                        random
                    ) && Mob.checkMobSpawnRules(entityType, level, spawnType, pos, random)
                },
                SpawnPlacementRegisterEvent.Operation.AND,
            )
        }

        registerBandit(ModEntities.BANDIT_REGULAR)
        registerBandit(ModEntities.BANDIT_ENFORCER)
    }
}