package dev.echoellet.dragonfist_legacy.entity.common.goals

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import java.util.*

class ApproachAndHoldPositionGoal(
    private val mob: Mob,
    private val speedForDistance: (distanceSqr: Double) -> SpeedContext,
    private val stopDistance: Double,
    private val approachTarget: () -> LivingEntity?,
) : Goal() {

    data class SpeedContext(val speed: Double, val isSprinting: Boolean)

    init {
        flags = EnumSet.of(Flag.MOVE)
    }

    override fun canUse(): Boolean = shouldActivate()
    override fun canContinueToUse(): Boolean = shouldActivate()

    private fun shouldActivate(): Boolean {
        return isTooFarFrom(mob.distanceToSqr(approachTarget() ?: return false))
                && mob.target == null
    }

    override fun start() {
        updateMovement()
    }

    private fun updateMovement() {
        val approachTarget = approachTarget() ?: return
        val distanceSqr = mob.distanceToSqr(approachTarget)
        if (isTooFarFrom(distanceSqr)) {
            val speedContext = speedForDistance(distanceSqr)
            mob.navigation.moveTo(approachTarget, speedContext.speed)
            mob.isAggressive = speedContext.isSprinting
        } else {
            mob.navigation.stop()
            mob.isAggressive = false
        }
    }

    override fun stop() {
        super.stop()
        mob.isAggressive = false
        mob.navigation.stop()
    }

    override fun tick() {
        updateMovement()
    }

    private fun isTooFarFrom(distanceSqr: Double): Boolean {
        return distanceSqr > (stopDistance * stopDistance)
    }
}
