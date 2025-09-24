package dev.echoellet.dragonfist_legacy.entity.shifu.combat

import dev.echoellet.dragonfist_legacy.entity.knight.KnightEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.util.isVillagerEntity
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.animal.IronGolem
import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.player.Player

/**
 * Custom [net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal] for Shifu.
 * - Never attacks vanilla or MCA villagers.
 * - Never attacks Iron Golems or Knights.
 * - Shows mercy to players at low health (spares them and sends messages).
 */
class ShifuMercifulHurtByTargetGoal(
    private val entity: ShifuEntity,
    private val playerTrainingPolicyProvider: () -> ShifuPlayerTrainingPolicy
) : HurtByTargetGoal(
    entity,
    // Never attacks vanilla villagers.
    Villager::class.java,
    KnightEntity::class.java,
    IronGolem::class.java,
) {
    private val playerTrainingPolicy get() = playerTrainingPolicyProvider()

    override fun canUse(): Boolean {

        val entity = entity.lastHurtByMob
        if (entity != null && entity.isVillagerEntity()) {
            return false
        }

        if (entity is Player && !playerTrainingPolicy.canStartTraining(entity)) {
            return false
        }
        return super.canUse()
    }

    override fun canContinueToUse(): Boolean {
        val currentTarget = entity.target

        if (currentTarget == null || currentTarget !is Player) {
            return super.canContinueToUse()
        }

        val shouldContinueFight = playerTrainingPolicy.handleTrainingContinuation(currentTarget)
        if (shouldContinueFight) {
            // Returning the result of [shouldContinueFight] directly could cause issues, fallback to super call.
            return super.canContinueToUse()
        }

        return false
    }
}