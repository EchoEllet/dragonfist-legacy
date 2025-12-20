package dev.echoellet.dragonfist_legacy.entity.common.goals

import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.player.Player

/**
 * Updates the [Mob.target] when it's this [mob] turn to fight the player,
 * to allow fighting one by one instead of all at once.
 */
class TurnBasedTargetSelectionGoal<T : Mob>(
    mob: T,
    private val mobType: Class<T>,
    mustSee: Boolean,
    private val maxFighters: Int,
    private val enterStandbyFor: (player: Player) -> Unit,
    private val exitStandby: () -> Unit,
) : NearestAttackableTargetGoal<Player>(mob, Player::class.java, mustSee) {

    override fun canUse(): Boolean {
        if (!super.canUse()) {
            return false
        }
        val player = nearestPlayerOrThrow
        val canJoin = canJoinFight(player)
        if (!canJoin) {
            // TODO: Bad safe effect, as the other target goals will continue to run,
            //  so if there is a villager and "canJoinFight" returns false,
            //  it will continue to force the entity to be in standby mode for the player,
            //  but the actual target is the villager. Read BanditEntity.setTarget() and other TODOs in that file
            waitMode(player)
            return false
        }
        return true
    }

    private val nearestPlayer: Player?
        get() = this.target?.let { it as Player }

    private val nearestPlayerOrThrow: Player
        get() = checkNotNull(nearestPlayer) { "Expected the nearest player target to be non-null" }

    override fun canContinueToUse(): Boolean {
        val player = nearestPlayerOrThrow
        if (!player.isAlive) return false
        return super.canContinueToUse()
    }

    override fun start() {
        super.start()
        attackMode(nearestPlayerOrThrow)
    }

    override fun stop() {
        super.stop()
        clear()
    }

    private fun searchForNearbyEntities(): List<Mob> {
        val searchArea = getTargetSearchArea(this.followDistance)
        val entities = this.mob.level()
            .getEntitiesOfClass(mobType, searchArea)
        return entities
    }

    private fun canJoinFight(player: Player): Boolean {
        val fighters = searchForNearbyEntities().count { it.target == player }
        return fighters < maxFighters
    }

    private fun attackMode(player: Player) {
        mob.target = player
        exitStandby()
    }

    private fun waitMode(player: Player) {
        mob.target = null
        enterStandbyFor(player)
    }

    private fun clear() {
        exitStandby()
        this.target = null
    }
}
