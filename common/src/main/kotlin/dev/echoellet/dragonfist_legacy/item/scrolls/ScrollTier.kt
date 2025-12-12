package dev.echoellet.dragonfist_legacy.item.scrolls

import kotlin.math.ceil

enum class ScrollTier(
    // Epic Fight ability points (https://www.curseforge.com/minecraft/mc-mods/epic-fight-skill-tree)
    val abilityPointContribution: Double, // contribution each scroll makes toward one skill ability point
) {
    VERY_COMMON(1.0 / 5),    // 5 scrolls = 1 point
    COMMON(1.0 / 3),         // 3 scrolls = 1 point
    UNCOMMON(1.0 / 2),       // 2 scrolls = 1 point
    RARE(1.0),               // 1 scroll = 1 point
    EPIC(2.0),               // 1 scroll = 2 points
    LEGENDARY(3.0);          // 1 scroll = 3 points

    // Minimum scrolls needed to get 1 ability point
    fun scrollsForOnePoint() = ceil(1.0 / abilityPointContribution).toInt()

    fun minAbilityPointsFromTrade(): Int {
        val minRequiredScrolls = scrollsForOnePoint()

        // Whether more than one scroll is needed for at least one ability point
        val isMultiScroll = minRequiredScrolls > 1 && abilityPointContribution < 1.0

        val minAbilityPointsFromTrade =
            if (isMultiScroll) (abilityPointContribution * minRequiredScrolls).toInt() else abilityPointContribution.toInt()

        return minAbilityPointsFromTrade
    }
}
