package dev.echoellet.dragonfist_legacy.entity.bandit.handler

import dev.echoellet.dragonfist_legacy.compatibility.MinecraftMod
import dev.echoellet.dragonfist_legacy.compatibility.epicfight.EpicFightModItems
import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.util.enchanted
import dev.echoellet.dragonfist_legacy.util.randomChance
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments

/**
 * Handles equipping a [BanditEntity] with armor according to its rank.
 * Armor is chosen based on predefined [ArmorOption]s for each rank.
 */
class BanditArmorEquipper(private val entity: BanditEntity) {
    companion object {
        /**
         * Represents a possible armor set with an optional chance.
         *
         * @property armorSet The full armor set (helmet, chestplate, leggings, boots) to equip.
         * @property chance Probability of equipping this set (0.0–1.0). Null means guaranteed.
         */
        private data class ArmorOption(
            val armorSet: ArmorSet,
            val chance: Double? // 0.0 - 1.0
        )

        /**
         * Represents a full armor set (helmet, chestplate, leggings, boots).
         *
         * Each enum value corresponds to a complete set of armor pieces.
         */
        private enum class ArmorSet {
            Leather,
            EpicFightStray,
            Golden,
            Chainmail,
            Iron,
            Diamond,
            Netherite,
        }

        /**
         * Returns the list of possible [ArmorOption]s for the given [rank].
         *
         * Lower ranks may have multiple options with probabilities.
         * Higher ranks typically have a guaranteed armor set.
         * [BanditRank.Ruler] returns null, meaning no armor is equipped.
         *
         * @param rank The bandit rank to get options for.
         * @return A list of [ArmorOption] or null if no armor should be equipped.
         */
        private fun getAvailableArmorOptionsForRank(rank: BanditRank): List<ArmorOption>? = when (rank) {
            BanditRank.Regular -> listOfNotNull(
                if (MinecraftMod.EPIC_FIGHT.isLoaded()) ArmorOption(ArmorSet.EpicFightStray, 0.35) else null,
                ArmorOption(ArmorSet.Leather, 0.2),
            )

            BanditRank.Enforcer -> listOf(
                ArmorOption(ArmorSet.Iron, 0.55),
                ArmorOption(ArmorSet.Chainmail, 0.45),
            )

            BanditRank.Champion -> listOf(
                ArmorOption(ArmorSet.Diamond, null),
            )

            BanditRank.Elite -> null // Elite bandit uses armored skin with armor attribute; actual armor items are not equipped.

            BanditRank.Leader -> listOf(
                ArmorOption(ArmorSet.Netherite, null),
            )

            BanditRank.Ruler -> null // No armor
        }

        /**
         * Holds the actual [Item]s for a full armor set.
         */
        data class ArmorSetItems(
            val helmet: Item,
            val chestplate: Item,
            val leggings: Item,
            val boots: Item,
        )

        /**
         * Returns the [ArmorSetItems] for a given [ArmorSet].
         *
         * @param armorSet The enum value representing the desired armor set.
         * @return An [ArmorSetItems] containing the specific [Item]s for each slot.
         */
        private fun resolveArmorSetItems(armorSet: ArmorSet): ArmorSetItems = when (armorSet) {
            ArmorSet.Leather -> ArmorSetItems(
                helmet = Items.LEATHER_HELMET,
                chestplate = Items.LEATHER_CHESTPLATE,
                leggings = Items.LEATHER_LEGGINGS,
                boots = Items.LEATHER_BOOTS
            )

            ArmorSet.EpicFightStray -> ArmorSetItems(
                helmet = EpicFightModItems.Stray.HAT.asItem(),
                chestplate = EpicFightModItems.Stray.ROBE.asItem(),
                leggings = EpicFightModItems.Stray.PANTS.asItem(),
                boots = Items.LEATHER_BOOTS
            )

            ArmorSet.Golden -> ArmorSetItems(
                helmet = Items.GOLDEN_HELMET,
                chestplate = Items.GOLDEN_CHESTPLATE,
                leggings = Items.GOLDEN_LEGGINGS,
                boots = Items.GOLDEN_BOOTS,
            )

            ArmorSet.Chainmail -> ArmorSetItems(
                helmet = Items.CHAINMAIL_HELMET,
                chestplate = Items.CHAINMAIL_CHESTPLATE,
                leggings = Items.CHAINMAIL_LEGGINGS,
                boots = Items.CHAINMAIL_BOOTS,
            )

            ArmorSet.Iron -> ArmorSetItems(
                helmet = Items.IRON_HELMET,
                chestplate = Items.IRON_CHESTPLATE,
                leggings = Items.IRON_LEGGINGS,
                boots = Items.IRON_BOOTS,
            )

            ArmorSet.Diamond -> ArmorSetItems(
                helmet = Items.DIAMOND_HELMET,
                chestplate = Items.DIAMOND_CHESTPLATE,
                leggings = Items.DIAMOND_LEGGINGS,
                boots = Items.DIAMOND_BOOTS,
            )

            ArmorSet.Netherite -> ArmorSetItems(
                helmet = Items.NETHERITE_HELMET,
                chestplate = Items.NETHERITE_CHESTPLATE,
                leggings = Items.NETHERITE_LEGGINGS,
                boots = Items.NETHERITE_BOOTS,
            )
        }
    }

    /**
     * Attempts to equip the bandit with a random armor set based on its rank.
     *
     * Uses chance values from [ArmorOption]. Guaranteed sets have [ArmorOption.chance] = null.
     * Call this method once during spawn.
     */
    fun mayEquipRandomArmorSet() {
        val banditRank = entity.getRank()
        val availableArmorOptions = getAvailableArmorOptionsForRank(banditRank) ?: return

        for (option in availableArmorOptions) {
            val shouldDrop = option.chance?.let { entity.random.randomChance(it) } ?: true
            if (!shouldDrop) continue

            equipArmorSet(option.armorSet)
            break
        }
    }

    /**
     * Returns an [ItemStack] for the given armor [item].
     *
     * Automatically enchants it with Protection III if the world difficulty is HARD.
     *
     * @param item The armor [Item] to convert to an [ItemStack].
     * @return The [ItemStack] ready to equip.
     */
    private fun createArmorItemStack(item: Item): ItemStack {
        val itemStack = ItemStack(item)
        val isHardDifficulty = entity.level().difficulty == Difficulty.HARD

        return when (isHardDifficulty) {
            true -> itemStack.enchanted(
                Enchantments.PROTECTION,
                3,
                entity.registryAccess()
            )

            false -> itemStack
        }
    }

    /**
     * Equips the bandit with a full armor set.
     *
     * @param armorSet The [ArmorSet] to equip.
     */
    private fun equipArmorSet(armorSet: ArmorSet) {
        val items = resolveArmorSetItems(armorSet)

        entity.setItemSlot(EquipmentSlot.HEAD, createArmorItemStack(items.helmet))
        entity.setItemSlot(EquipmentSlot.CHEST, createArmorItemStack(items.chestplate))
        entity.setItemSlot(EquipmentSlot.LEGS, createArmorItemStack(items.leggings))
        entity.setItemSlot(EquipmentSlot.FEET, createArmorItemStack(items.boots))
    }
}
