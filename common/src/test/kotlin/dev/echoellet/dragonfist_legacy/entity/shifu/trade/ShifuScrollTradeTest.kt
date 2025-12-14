package dev.echoellet.dragonfist_legacy.entity.shifu.trade

import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollTier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ShifuScrollTradeTest {
    private fun tryTrade(tier: ScrollTier, playerScrolls: Int): ShifuScrollTrade.TradeResult? =
        ShifuScrollTrade.tryTrade(tier, playerScrolls)

    private fun assertRejectedRange(tier: ScrollTier, range: IntRange) {
        for (scrolls in range) {
            assertNull(tryTrade(tier, scrolls), "Expected rejection for $scrolls $tier scrolls")
        }
    }

    private fun assertApprovedTrade(
        tier: ScrollTier,
        playerScrolls: Int,
        expectedScrolls: Int,
        expectedPoints: Int
    ) {
        val result = tryTrade(tier, playerScrolls)
        assertNotNull(result, "Expected trade approval but got null")
        assertEquals(expectedScrolls, result.scrollsConsumed)
        assertEquals(expectedPoints, result.abilityPointsGained)
    }

    // Very common

    @Test
    fun `approves 5 very common scrolls for 1 ability point`() =
        assertApprovedTrade(ScrollTier.VERY_COMMON, playerScrolls = 5, expectedScrolls = 5, expectedPoints = 1)

    @Test
    fun `rejects very common scrolls less than 5 for 1 ability point`() =
        assertRejectedRange(ScrollTier.VERY_COMMON, range = 0..4)


    // Common

    @Test
    fun `approves 3 common scrolls for 1 ability point`() =
        assertApprovedTrade(ScrollTier.COMMON, playerScrolls = 3, expectedScrolls = 3, expectedPoints = 1)

    @Test
    fun `rejects common scrolls less than 3 for 1 ability point`() =
        assertRejectedRange(ScrollTier.COMMON, range = 0..2)


    // Uncommon

    @Test
    fun `approves 2 uncommon scrolls for 1 ability point`() =
        assertApprovedTrade(ScrollTier.UNCOMMON, playerScrolls = 2, expectedScrolls = 2, expectedPoints = 1)

    @Test
    fun `rejects uncommon scrolls less than 2 for 1 ability point`() =
        assertRejectedRange(ScrollTier.UNCOMMON, range = 0..1)


    // Rare

    @Test
    fun `approves 1 rare scroll for 1 ability point`() =
        assertApprovedTrade(ScrollTier.RARE, playerScrolls = 1, expectedScrolls = 1, expectedPoints = 1)

    @Test
    fun `rejects rare scrolls less than 1 for 1 ability point`() =
        assertRejectedRange(ScrollTier.RARE, range = 0..0)


    // Epic

    @Test
    fun `approves 1 epic scroll for 2 ability points`() =
        assertApprovedTrade(ScrollTier.EPIC, playerScrolls = 1, expectedScrolls = 1, expectedPoints = 2)

    @Test
    fun `rejects epic scrolls less than 1 for 2 ability points`() =
        assertRejectedRange(ScrollTier.EPIC, range = 0..0)


    // Legendary

    @Test
    fun `approves 1 legendary scroll for 3 ability points`() =
        assertApprovedTrade(ScrollTier.LEGENDARY, playerScrolls = 1, expectedScrolls = 1, expectedPoints = 3)

    @Test
    fun `rejects legendary scrolls less than 1 for 3 ability points`() =
        assertRejectedRange(ScrollTier.LEGENDARY, range = 0..0)
}
