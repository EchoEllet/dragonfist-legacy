package dev.echoellet.dragonfist_legacy.entity.shifu.trade

import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollTier

/**
 * Handles trading scrolls of different tiers for ability points.
 */
object ShifuScrollTrade {
    /**
     * Tries to trade scrolls of this tier for ability points.
     *
     * @param tier The scroll tier being traded.
     * @param playerScrolls How many scrolls the player currently has.
     * @return The result of the trade: points gained and scrolls consumed.
     *         If the player does not have enough scrolls, returns null.
     */
    fun tryTrade(tier: ScrollTier, playerScrolls: Int): TradeResult? {
        val minRequiredScrolls = tier.scrollsForOnePoint()

        if (playerScrolls < minRequiredScrolls) {
            // Player don't have enough scrolls for at least one point
            return null
        }

        val minAbilityPointsFromTrade = tier.minAbilityPointsFromTrade()

        return TradeResult(
            scrollsConsumed = minRequiredScrolls,
            abilityPointsGained = minAbilityPointsFromTrade
        )
    }

    /**
     * Represents the result of trading scrolls with Shifu.
     */
    data class TradeResult(
        val abilityPointsGained: Int,
        val scrollsConsumed: Int
    )
}
