package dev.echoellet.dragonfist_legacy.entity.bandit.handler

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.registry.entries.item.ModItems
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
            return when (rank) {
                BanditRank.Regular -> listOf(
                    LootEntry(Items.EMERALD, 2, 4, 0.5),
                    LootEntry(Items.GOLD_INGOT, 1, 2, 0.25),
                    LootEntry(Items.IRON_INGOT, 2, 5, 0.5),
                    LootEntry(Items.DIAMOND, 1, 2, 0.15),
                    LootEntry(Items.FIREWORK_ROCKET, 2, 4, 0.35),
                    LootEntry(ModItems.SUMMON_KNIGHT.get(), 1, 1, 0.15),
                )

                BanditRank.Enforcer -> listOf(
                    LootEntry(Items.EMERALD, 4, 8, 0.6),
                    LootEntry(Items.GOLD_INGOT, 2, 4, 0.35),
                    LootEntry(Items.IRON_INGOT, 3, 6, 0.7),
                    LootEntry(Items.DIAMOND, 2, 4, 0.35),
                    LootEntry(Items.FIREWORK_ROCKET, 4, 8, 0.5),
                    LootEntry(Items.GOLDEN_APPLE, 1, 4, 0.35),
                    LootEntry(ModItems.SUMMON_KNIGHT.get(), 1, 1, 0.45),
                )

                BanditRank.Champion -> listOf(
                    LootEntry(Items.EMERALD, 6, 12, 0.65),
                    LootEntry(Items.GOLD_INGOT, 3, 6, 0.5),
                    LootEntry(Items.IRON_INGOT, 4, 8, 0.5),
                    LootEntry(Items.DIAMOND, 3, 6, 0.45),
                    LootEntry(Items.FIREWORK_ROCKET, 8, 16, 0.65),
                    LootEntry(Items.ENCHANTED_BOOK, 1, 1, 0.2),
                    LootEntry(Items.ENCHANTED_GOLDEN_APPLE, 1, 1, 0.45),
                    LootEntry(Items.GOLDEN_APPLE, 1, 2, 0.55),
                    LootEntry(ModItems.SUMMON_KNIGHT.get(), 1, 1, 0.65),
                )

                BanditRank.Elite -> listOf(
                    LootEntry(Items.EMERALD, 8, 14, 0.65),
                    LootEntry(Items.GOLD_INGOT, 5, 10, 0.45),
                    LootEntry(Items.IRON_INGOT, 6, 12, 0.4),
                    LootEntry(Items.DIAMOND_BLOCK, 1, 2, 0.3),
                    LootEntry(Items.NETHERITE_SCRAP, 1, 2, 0.65),
                    LootEntry(Items.FIREWORK_ROCKET, 16, 32, 0.75),
                    LootEntry(Items.ENCHANTED_GOLDEN_APPLE, 1, 2, 0.5),
                    LootEntry(Items.GOLDEN_APPLE, 2, 8, 0.75),
                    LootEntry(ModItems.SUMMON_KNIGHT.get(), 1, 2, 0.95),
                )

                BanditRank.Leader -> listOf(
                    LootEntry(Items.EMERALD, 10, 18, 0.7),
                    LootEntry(Items.GOLD_INGOT, 6, 12, 0.5),
                    LootEntry(Items.IRON_INGOT, 8, 14, 0.45),
                    LootEntry(Items.DIAMOND_BLOCK, 2, 3, 0.35),
                    LootEntry(Items.NETHERITE_INGOT, 1, 1, 0.25),
                    LootEntry(Items.ENCHANTED_GOLDEN_APPLE, 1, 1, 0.05),
                    LootEntry(Items.FIREWORK_ROCKET, 32, 48, 0.85),
                    LootEntry(ModItems.SUMMON_KNIGHT.get(), 2, 2, 0.95),
                )

                BanditRank.Ruler -> listOf(
                    LootEntry(Items.EMERALD, 16, 32, 0.75),
                    LootEntry(Items.GOLD_BLOCK, 2, 4, 0.6),
                    LootEntry(Items.IRON_BLOCK, 2, 4, 0.55),
                    LootEntry(Items.DIAMOND_BLOCK, 4, 6, 0.45),
                    LootEntry(Items.NETHERITE_INGOT, 1, 3, 0.5),
                    LootEntry(Items.TOTEM_OF_UNDYING, 1, 1, 0.25),
                    LootEntry(Items.FIREWORK_ROCKET, 32, 64, 0.95),
                    LootEntry(ModItems.SUMMON_KNIGHT.get(), 2, 4, 0.95),
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
