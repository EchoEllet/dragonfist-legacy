package dev.echoellet.dragonfist_legacy.entity.shifu.trade

import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollTier

data class ScrollTradeInfo(
    val scrollsRequired: Int,
    val abilityPoints: Int
) {
    companion object {
        fun byTier(tier: ScrollTier): ScrollTradeInfo {
            return ScrollTradeInfo(
                scrollsRequired = tier.scrollsForOnePoint(),
                abilityPoints = tier.minAbilityPointsFromTrade()
            )
        }
    }
}
