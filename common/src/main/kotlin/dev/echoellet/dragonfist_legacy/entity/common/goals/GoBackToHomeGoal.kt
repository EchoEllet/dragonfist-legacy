package dev.echoellet.dragonfist_legacy.entity.common.goals

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import java.util.*

class GoBackToHomeGoal(
    private val mob: Mob,
    private val nearHomeDistance: Double,
    private val speed: Double,
    private val homePos: () -> BlockPos,
    private val shouldGoHome: () -> Boolean,
) : Goal() {
    init {
        flags = EnumSet.of(Flag.MOVE)
    }

    override fun canUse(): Boolean {
        if (!shouldGoHome()) {
            /**
             * **Important:** Call [shouldGoHome] before [isNearHome], since [homePos]
             * may throw an exception if [shouldGoHome] is `false`.
             */
            return false
        }
        val hasNoTarget = mob.target == null
        return hasNoTarget && !isNearHome()
    }

    override fun canContinueToUse(): Boolean {
        return super.canContinueToUse() && !mob.navigation.isDone
    }

    override fun start() {
        super.start()
        navigate()
    }

    override fun tick() {
        super.tick()
        if (mob.navigation.isDone) navigate()
    }

    private fun navigate() {
        val pos = homePos()
        mob.navigation.moveTo(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), speed)
    }

    private fun isNearHome(): Boolean {
        return mob.blockPosition().closerThan(homePos(), nearHomeDistance)
    }
}
