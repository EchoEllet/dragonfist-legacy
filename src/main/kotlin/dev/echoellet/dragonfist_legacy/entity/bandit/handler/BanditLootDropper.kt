package dev.echoellet.dragonfist_legacy.entity.bandit.handler

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.util.randomChance
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

/**
 * Handles general loot drops for a [BanditEntity] based on its [BanditRank].
 *
 * This class manages dropping common loot items (e.g., emeralds, gold, iron, diamonds)
 * according to rank-specific loot tables. Each [LootEntry] defines the item,
 * minimum and maximum drop count, and drop chance.
 *
 * NOTE: This is intended for **generic loot only**. For specialized drops
 * such as scrolls, use a dedicated class (e.g., [BanditScrollDropper]).
 */
class BanditLootDropper(private val entity: BanditEntity) {
    companion object {
        /**
         * Represents a single loot entry in a loot table.
         *
         * @param item The type of item to drop.
         * @param minCount Minimum number of items to drop.
         * @param maxCount Maximum number of items to drop.
         * @param chance Probability (0.0 to 1.0) that this item will drop.
         */
        data class LootEntry(
            val item: Item,
            val minCount: Int,
            val maxCount: Int,
            val chance: Double, // 0.0 to 1.0
        )

        /**
         * Returns the loot table for a given [BanditRank].
         *
         * Each rank has a distinct set of possible loot drops, quantities, and chances.
         *
         * @param rank The rank of the bandit.
         * @return A list of [LootEntry] defining the possible drops for that rank.
         */
        fun getLootTableFor(rank: BanditRank): List<LootEntry> {
            return when(rank) {
                BanditRank.Regular -> listOf(
                    LootEntry(Items.EMERALD, 1, 2, 0.65),
                    LootEntry(Items.GOLD_INGOT, 1, 2, 0.1),
                    LootEntry(Items.IRON_INGOT, 1, 4, 0.45),
                )
                BanditRank.Enforcer -> listOf(
                    LootEntry(Items.EMERALD, 2, 8, 0.45),
                    LootEntry(Items.GOLD_INGOT, 2, 6, 0.2),
                )
                BanditRank.Champion -> listOf(
                    LootEntry(Items.EMERALD, 4, 8, 0.5),
                    LootEntry(Items.GOLD_INGOT, 2, 8, 0.25),
                    LootEntry(Items.IRON_INGOT, 2, 6, 0.35),
                )
                BanditRank.Elite -> listOf(
                    LootEntry(Items.EMERALD, 6, 8, 0.6),
                    LootEntry(Items.GOLD_INGOT, 4, 8, 0.3),
                    LootEntry(Items.DIAMOND, 1, 2, 0.1),
                    LootEntry(Items.IRON_INGOT, 5, 10, 0.25),
                )
                BanditRank.Leader -> listOf(
                    LootEntry(Items.EMERALD, 6, 8, 0.6),
                    LootEntry(Items.GOLD_INGOT, 6, 12, 0.5),
                    LootEntry(Items.DIAMOND, 1, 4, 0.25),
                    LootEntry(Items.IRON_INGOT, 6, 10, 0.45),
                )
                BanditRank.Ruler -> listOf(
                    LootEntry(Items.EMERALD, 8, 20, 0.65),
                    LootEntry(Items.GOLD_INGOT, 16, 24, 0.65),
                    LootEntry(Items.DIAMOND, 2, 12, 0.45),
                    LootEntry(Items.IRON_INGOT, 24, 32, 0.75),
                )
            }
        }
    }

    /**
     * Rolls for loot based on the bandit's rank and drops the resulting items at the bandit's location.
     *
     * Each loot entry in the rank's loot table is rolled individually according to its chance.
     * If the roll succeeds, a random quantity within [minCount, maxCount] is spawned.
     *
     * This method should be called when the bandit dies.
     */
    fun chanceToDropLoot() {
        val banditRank = entity.getRank()
        val lootEntries = getLootTableFor(rank = banditRank)
        val random = entity.random

        for (entry in lootEntries) {
            val shouldDrop = random.randomChance(entry.chance)
            if (!shouldDrop) continue

            val count = random.nextInt(entry.minCount, entry.maxCount + 1)
            spawnLoot(entry.item, count)
        }
    }

    /**
     * Spawns the given item with the specified count at the bandit's location.
     *
     * @param item The item to spawn.
     * @param count The quantity of the item to spawn.
     */
    private fun spawnLoot(item: Item, count: Int) {
        val itemStack = ItemStack(item, count)
        entity.spawnAtLocation(itemStack)
    }
}
