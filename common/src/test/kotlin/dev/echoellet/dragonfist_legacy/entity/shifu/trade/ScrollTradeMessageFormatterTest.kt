package dev.echoellet.dragonfist_legacy.entity.shifu.trade

import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollTier
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

class ScrollTradeMessageFormatterTest {
    private fun formatForTier(tier: ScrollTier): String {
        val info = ScrollTradeInfo.byTier(tier)
        return ScrollTradeMessageFormatter.format(info)
    }

    // Note: This test suite is not only testing ScrollTradeMessageFormatter.
    // It also validates the pricing rules defined in ScrollTradeInfo.byTier
    // for each ScrollTier, ensuring both data and formatting stay consistent.

    // Very Common

    @Test
    fun `formats very common pricing as 5 scrolls = 1 point`() {
        assertEquals("5 scrolls → 1 point", formatForTier(ScrollTier.VERY_COMMON))
    }

    // Common

    @Test
    fun `formats common pricing as 3 scrolls = 1 point`() {
        assertEquals("3 scrolls → 1 point", formatForTier(ScrollTier.COMMON))
    }

    // Uncommon

    @Test
    fun `formats uncommon pricing as 2 scrolls = 1 point`() {
        assertEquals("2 scrolls → 1 point", formatForTier(ScrollTier.UNCOMMON))
    }

    // Rare

    @Test
    fun `formats rare pricing as 1 scroll = 1 point`() {
        assertEquals("1 scroll → 1 point", formatForTier(ScrollTier.RARE))
    }

    // Epic

    @Test
    fun `formats epic pricing as 1 scroll = 2 points`() {
        assertEquals("1 scroll → 2 points", formatForTier(ScrollTier.EPIC))
    }

    // Legendary

    @Test
    fun `formats legendary pricing as 1 scroll = 3 points`() {
        assertEquals("1 scroll → 3 points", formatForTier(ScrollTier.LEGENDARY))
    }
}
