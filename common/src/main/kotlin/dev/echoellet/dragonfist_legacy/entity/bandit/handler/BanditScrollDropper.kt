package dev.echoellet.dragonfist_legacy.entity.bandit.handler

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.registry.entries.item.ModItems
import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollTier
import dev.echoellet.dragonfist_legacy.util.randomChance
import net.minecraft.world.item.ItemStack

/**
 * Handles the logic for scroll drops when a [BanditEntity] is killed.
 *
 * Scroll drops are determined by the bandit's rank and each scroll tier has
 * a defined drop chance. Multiple tiers can potentially drop from a single bandit.
 */
class BanditScrollDropper(private val entity: BanditEntity) {
    companion object {
        /**
         * Represents a scroll drop option with its tier and drop chance.
         *
         * @property tier The [ScrollTier] of the scroll.
         * @property chance The probability (0.0–1.0) of dropping this scroll.
         */
        private data class ScrollDrop(
            val tier: ScrollTier,
            val chance: Double, // 0.0 - 1.0
        )

        /**
         * Returns the [ScrollItem] instance corresponding to the given [ScrollTier].
         *
         * @param tier The tier of scroll item to retrieve.
         * @return The [ScrollItem] corresponding to the [tier].
         */
        private fun getScrollItemByTier(tier: ScrollTier): ScrollItem = when (tier) {
            ScrollTier.VERY_COMMON -> ModItems.VERY_COMMON_SCROLL
            ScrollTier.COMMON -> ModItems.COMMON_SCROLL
            ScrollTier.UNCOMMON -> ModItems.UNCOMMON_SCROLL
            ScrollTier.RARE -> ModItems.RARE_SCROLL
            ScrollTier.EPIC -> ModItems.EPIC_SCROLL
            ScrollTier.LEGENDARY -> ModItems.LEGENDARY_SCROLL
        }.get()

        /**
         * Returns the list of possible scroll drops for a given [BanditRank].
         *
         * Each rank has different tiers and chances. Multiple scrolls may drop
         * independently depending on their chances.
         *
         * @param rank The bandit rank.
         * @return A list of [ScrollDrop] for this rank.
         */
        private fun getScrollDropsForRank(rank: BanditRank): List<ScrollDrop> = when (rank) {
            BanditRank.Regular -> listOf(
                ScrollDrop(ScrollTier.VERY_COMMON, 0.2),
                ScrollDrop(ScrollTier.COMMON, 0.05),
            )

            BanditRank.Enforcer -> listOf(
                ScrollDrop(ScrollTier.COMMON, 0.2),
                ScrollDrop(ScrollTier.UNCOMMON, 0.15),
            )

            BanditRank.Champion -> listOf(
                ScrollDrop(ScrollTier.UNCOMMON, 0.25),
                ScrollDrop(ScrollTier.RARE, 0.05),
            )

            BanditRank.Elite -> listOf(
                ScrollDrop(ScrollTier.RARE, 0.4),
                ScrollDrop(ScrollTier.EPIC, 0.05),
            )

            BanditRank.Leader -> listOf(
                ScrollDrop(ScrollTier.EPIC, 0.65),
                ScrollDrop(ScrollTier.LEGENDARY, 0.001)
            )

            BanditRank.Ruler -> listOf(
                ScrollDrop(ScrollTier.LEGENDARY, 0.95)
            )
        }
    }

    /**
     * Determines which scrolls should drop based on the bandit's rank and
     * the defined drop chances, then spawns them at the bandit's location.
     *
     * This method should be called when the bandit dies.
     */
    fun chanceToDropScrolls() {
        val banditRank = entity.getRank()
        val scrollDrops = getScrollDropsForRank(banditRank)

        for (drop in scrollDrops) {
            val shouldDrop = entity.random.randomChance(drop.chance)
            if (!shouldDrop) continue
            spawnScrollItem(drop.tier)
        }
    }

    /**
     * Spawns a scroll item of the given tier at the bandit's location.
     *
     * @param tier The [ScrollTier] of the scroll to spawn.
     */
    private fun spawnScrollItem(tier: ScrollTier) {
        val item: ScrollItem = getScrollItemByTier(tier)

        val itemStack = ItemStack(item)
        entity.spawnAtLocation(itemStack)
    }
}
