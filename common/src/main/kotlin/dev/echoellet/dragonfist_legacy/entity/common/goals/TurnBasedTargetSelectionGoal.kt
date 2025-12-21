package dev.echoellet.dragonfist_legacy.entity.common.goals

import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.player.Player

/**
 * Updates the [Mob.target] when it's this [mob] turn to fight the player,
 * to allow fighting one by one instead of all at once.
 *
 * This will update [NearestAttackableTargetGoal.target] to be non-null as soon as it finds
 * a nearby player, and once it's time for [mob] to fight the player,
 * [Mob.target] will be non-null (same value) as well.
 *
 * **Internal note:** When [Mob.target] is:
 *  - **not `null`:** then [exitStandby] must be called, as vanilla AI will be responsible for movements.
 *  - **`null`:** then [enterStandbyFor] must be called, and vanilla AI should not affect movements.
 *
 * This is used in combination with [ApproachAndHoldPositionGoal], however, to keep it decoupled,
 * [enterStandbyFor] and [exitStandby] are required.
 *
 * @see dev.echoellet.dragonfist_legacy.entity.common.MobFocusManager
 */
class TurnBasedTargetSelectionGoal<T : Mob>(
    mob: T,
    private val mobType: Class<T>,
    mustSee: Boolean,
    private val maxFighters: Int,
    private val enterStandbyFor: (player: Player) -> Unit,
    private val exitStandby: () -> Unit,
) : NearestAttackableTargetGoal<Player>(mob, Player::class.java, mustSee) {

    /**
     * Returns [NearestAttackableTargetGoal.mob] cast as a [Player].
     */
    private val nearestPlayer: Player?
        get() = this.target?.let { it as Player }

    private val nearestPlayerOrThrow: Player
        get() = checkNotNull(nearestPlayer) { "Expected the nearest player target to be non-null" }

    override fun canContinueToUse(): Boolean {
        val player = nearestPlayerOrThrow
        if (!player.isAlive) return false

        val canContinueToUse = super.canContinueToUse()
        if (!canContinueToUse && (this.mob.target == null && nearestPlayer != null)) {
            /**
             * Workaround to prevent [stop] from being called due to [Mob.target] being `null`.
             *
             * [Mob.target] is intentionally set to `null` to prevent the entity from fighting the [target],
             * even though [NearestAttackableTargetGoal.target] is not `null`.
             *
             * [net.minecraft.world.entity.ai.goal.target.TargetGoal] will always return `false`
             * if [Mob.target] is `null`.
             */
            return true
        }
        return canContinueToUse
    }

    override fun start() {
        super.start()
        /**
         * Undo the side effect of [NearestAttackableTargetGoal.start] (`super.start()`)
         * of setting [Mob.target] to [NearestAttackableTargetGoal.target],
         * which is unwanted, as it will cause the entity to always fight the target,
         * and therefore a violation of the core concept of this class's goal.
         *
         * The `super.start()` above mainly exists for compatibility reasons with vanilla or other mods.
         *
         * [Mob.target] is set to non-null by [updateCombatTarget], not [start].
         */
        this.mob.target = null
    }

    override fun stop() {
        super.stop()
        clear()
    }

    override fun tick() {
        super.tick()
        updateCombatTarget()
    }

    /**
     * Updates [Mob.target] (of this [mob]) based on whether the mob can join the fight.
     * - If the mob can join, it enters attack mode; does nothing afterward until mob's death.
     * - Otherwise, it waits, keeping [Mob.target] `null`.
     *
     * The target is taken from [NearestAttackableTargetGoal.target].
     */
    private fun updateCombatTarget() {
        if (mob.target != null) return
        val player = nearestPlayerOrThrow
        if (canJoinFight(player)) attackMode(player) else waitMode(player)
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
