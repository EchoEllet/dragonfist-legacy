package dev.echoellet.dragonfist_legacy.entity.common.goals

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.level.Level
import java.util.*

class GoHomeAtNightGoal(
    private val mob: Mob,
    private val nearHomeDistance: Double,
    private val homePos: () -> BlockPos,
) : Goal() {
    init {
        flags = EnumSet.of(Flag.MOVE)
    }

    override fun canUse(): Boolean {
        val level: Level = mob.level()
        val isNight = level.isNight || level.dayTime % 24000L >= 12000L - 2000L
        val hasNoTarget = mob.target == null

        return isNight && hasNoTarget && !isAtHome()
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
        mob.navigation.moveTo(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 1.0)
    }

    private fun isAtHome(): Boolean {
        return mob.blockPosition().closerThan(homePos(), nearHomeDistance)
    }
}
