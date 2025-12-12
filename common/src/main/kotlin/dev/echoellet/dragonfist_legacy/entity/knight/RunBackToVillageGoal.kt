package dev.echoellet.dragonfist_legacy.entity.knight

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.core.SectionPos
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.behavior.BehaviorUtils
import net.minecraft.world.entity.ai.goal.Goal

/**
 * A variation of [net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal]
 * where the mob runs directly to the nearest village section instead of
 * strolling randomly toward it.
 * */
class RunBackToVillageGoal(
    private val mob: PathfinderMob,
    private val speedModifier: Double
) : Goal() {

    private var targetPos: BlockPos? = null

    override fun canUse(): Boolean {
        val level = mob.level() as ServerLevel
        if (level.isVillage(mob.blockPosition())) return false

        val currentSection = SectionPos.of(mob.blockPosition())
        val villageSection = BehaviorUtils.findSectionClosestToVillage(level, currentSection, 2)

        return if (villageSection != currentSection) {
            targetPos = villageSection.center()
            true
        } else false
    }

    override fun start() {
        targetPos?.let {
            mob.navigation.moveTo(
                it.x.toDouble(),
                it.y.toDouble(),
                it.z.toDouble(),
                speedModifier,
            )
        }
    }

    override fun canContinueToUse(): Boolean {
        return !mob.navigation.isDone && targetPos != null
    }
}
