package dev.echoellet.dragonfist_legacy.entity.knight

import dev.echoellet.dragonfist_legacy.Config
import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.util.constants.Constants
import net.minecraft.core.BlockPos
import net.minecraft.server.TickTask
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.levelgen.Heightmap

/**
 * Handles spawning Knights in villages.
 *
 * WORKAROUND: Since there is no reliable built-in way to spawn knights in villages,
 * this event handler spawns knights whenever an Iron Golem naturally spawns
 * in a village structure (or any structure).
 *
 * Notes:
 * - Ignores player-created Iron Golems to prevent unwanted knight spawning.
 * - Spawns knights near the Iron Golem spawn position with a random offset.
 */
object VillageKnightSpawnEventHandler {
    fun onFinalizeSpawn(entity: Mob, spawnType: MobSpawnType, level: ServerLevelAccessor) {
        if (entity !is IronGolem) return

        if (entity.isPlayerCreated || spawnType != MobSpawnType.STRUCTURE) return

        val serverLevel: ServerLevel = level.level ?: kotlin.run {
            DragonFistLegacy.LOGGER.error(
                "An unexpected error while trying to spawn Knights in the village." +
                        "The ServerLevel is null, please consider reporting this issue with the details: ${Constants.ISSUE_REPORT}"
            )
            return
        }

        /**
         * Without this 1-tick delay workaround, spawning a knight entity at a position
         * near the Iron Golem during this event will freeze the game and make it unresponsive.
         * Spawning the knight at a different, already-loaded position does not cause this issue.
         *
         * WARNING: This isn't an efficient solution and may cause issues or game freezes in the future.
         */
        serverLevel.server.tell(TickTask(1) {
            spawnKnights(
                serverLevel = serverLevel,
                ironGolemPos = entity.blockPosition(),
            )
        })
    }

    private fun getKnightsToSpawn(random: RandomSource): Int {
        val minKnights = Config.MIN_KNIGHTS_SPAWN.get()
        val maxKnights = Config.MAX_KNIGHTS_SPAWN.get()
        val knightsToSpawn = minKnights + random.nextInt(maxKnights - minKnights + 1)
        return knightsToSpawn
    }

    private fun spawnKnights(
        ironGolemPos: BlockPos,
        serverLevel: ServerLevel
    ) {
        val knightEntityType = ModEntities.KNIGHT.get()
        val knightsToSpawn = getKnightsToSpawn(serverLevel.random)

        repeat(knightsToSpawn) {
            spawnKnightAtRandomLocation(
                ironGolemPos = ironGolemPos,
                knightEntityType = knightEntityType,
                serverLevel = serverLevel,
            )
        }
    }

    private fun spawnKnightAtRandomLocation(
        ironGolemPos: BlockPos,
        knightEntityType: EntityType<KnightEntity>,
        serverLevel: ServerLevel
    ) {
        val x = ironGolemPos.x + serverLevel.random.nextInt(-5, 6)
        val z = ironGolemPos.z + serverLevel.random.nextInt(-5, 6)
        val y = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z)

        val spawnPositon = BlockPos(x, y, z)

        knightEntityType.spawn(
            serverLevel,
            spawnPositon,
            MobSpawnType.STRUCTURE,
        )
    }
}
