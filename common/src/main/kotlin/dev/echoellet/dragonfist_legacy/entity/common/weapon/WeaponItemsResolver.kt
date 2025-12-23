package dev.echoellet.dragonfist_legacy.entity.common.weapon

import dev.echoellet.dragonfist_legacy.compatibility.epicfight.EpicFightModItems
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

/**
 * Represents the material tier of a weapon.
 *
 * Each enum value corresponds to a standard Minecraft weapon tier
 * (e.g., Wooden, Iron, Diamond, Netherite).
 */
enum class WeaponTier {
    Wooden,
    Stone,
    Golden,
    Iron,
    Diamond,
    Netherite,
}

/**
 * Represents all possible weapon loadouts a living entity can spawn with.
 *
 * This is a sealed hierarchy; all loadouts must be defined here.
 */
sealed interface WeaponLoadout {
    /**
     * Vanilla Minecraft weapon loadouts.
     */
    sealed interface Vanilla : WeaponLoadout {
        data class Sword(val tier: WeaponTier, val withShield: Boolean = false) : Vanilla
        data class TwoSwords(val tier: WeaponTier) : Vanilla
        data class Axe(val tier: WeaponTier) : Vanilla
        data object Trident : Vanilla

        // Currently unsupported; living entities of this mod must not use their bow/crossbow
        data object Bow : Vanilla
        data object Crossbow : Vanilla
    }

    /**
     * Epic Fight mod-specific weapon loadouts.
     * See also: https://github.com/Epic-Fight/epicfight/blob/1.21.1/src/main/java/yesman/epicfight/registry/entries/EpicFightItems.java#L27-L141
     */
    sealed interface EpicFight : WeaponLoadout {
        data object Gloves : EpicFight
        data object Uchigatana : EpicFight
        data class Greatsword(val tier: WeaponTier) : EpicFight
        data class Longsword(val tier: WeaponTier, val withShield: Boolean = false) : EpicFight
        data class Spear(val tier: WeaponTier, val withShield: Boolean = false) : EpicFight
        data class Tachi(val tier: WeaponTier) : EpicFight
        data class Daggers(val tier: WeaponTier) : EpicFight
        data object TwoBokken : EpicFight
    }
}

/**
 * Holds the actual [Item]s for a weapon loadout.
 *
 * @property mainHand The weapon for the main hand.
 * @property offhand The weapon for the offhand.
 */
data class WeaponItems(
    val mainHand: Item?,
    val offhand: Item?,
)

object WeaponItemsResolver {
    private fun epicFightDualWield(itemId: EpicFightModItems.ItemId): WeaponItems {
        val item = itemId.asItem()
        return WeaponItems(item, item)
    }

    private fun epicFightSingleHand(itemId: EpicFightModItems.ItemId): WeaponItems =
        WeaponItems(itemId.asItem(), null)

    private fun dualWield(item: Item): WeaponItems =
        WeaponItems(item, item)

    private fun singleHand(item: Item): WeaponItems = WeaponItems(item, null)

    private fun WeaponItems.maybeAddShield(add: Boolean): WeaponItems =
        copy(offhand = if (add) Items.SHIELD else null)

    private fun getSwordItemForTier(tier: WeaponTier) = when (tier) {
        WeaponTier.Wooden -> Items.WOODEN_SWORD
        WeaponTier.Stone -> Items.STONE_SWORD
        WeaponTier.Golden -> Items.GOLDEN_SWORD
        WeaponTier.Iron -> Items.IRON_SWORD
        WeaponTier.Diamond -> Items.DIAMOND_SWORD
        WeaponTier.Netherite -> Items.NETHERITE_SWORD
    }

    /**
     * Resolves a [WeaponLoadout] into actual [WeaponItems] to equip.
     *
     * Handles both vanilla and Epic Fight mod weapons.
     *
     * @param loadout The loadout to resolve.
     * @return [WeaponItems] containing main hand and offhand weapons.
     */
    fun resolve(loadout: WeaponLoadout): WeaponItems = when (loadout) {
        WeaponLoadout.EpicFight.TwoBokken -> epicFightDualWield(EpicFightModItems.BOKKEN)
        is WeaponLoadout.EpicFight.Daggers -> epicFightDualWield(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModItems.Daggers.WOODEN
                WeaponTier.Stone -> EpicFightModItems.Daggers.STONE
                WeaponTier.Golden -> EpicFightModItems.Daggers.GOLDEN
                WeaponTier.Iron -> EpicFightModItems.Daggers.IRON
                WeaponTier.Diamond -> EpicFightModItems.Daggers.DIAMOND
                WeaponTier.Netherite -> EpicFightModItems.Daggers.NETHERITE
            }
        )

        WeaponLoadout.EpicFight.Gloves -> epicFightDualWield(EpicFightModItems.GLOVE)
        is WeaponLoadout.EpicFight.Greatsword -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModItems.GreatSword.WOODEN
                WeaponTier.Stone -> EpicFightModItems.GreatSword.STONE
                WeaponTier.Golden -> EpicFightModItems.GreatSword.GOLDEN
                WeaponTier.Iron -> EpicFightModItems.GreatSword.IRON
                WeaponTier.Diamond -> EpicFightModItems.GreatSword.DIAMOND
                WeaponTier.Netherite -> EpicFightModItems.GreatSword.NETHERITE
            }
        )

        is WeaponLoadout.EpicFight.Longsword -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModItems.Longsword.WOODEN
                WeaponTier.Stone -> EpicFightModItems.Longsword.STONE
                WeaponTier.Golden -> EpicFightModItems.Longsword.GOLDEN
                WeaponTier.Iron -> EpicFightModItems.Longsword.IRON
                WeaponTier.Diamond -> EpicFightModItems.Longsword.DIAMOND
                WeaponTier.Netherite -> EpicFightModItems.Longsword.NETHERITE
            }
        ).maybeAddShield(loadout.withShield)

        is WeaponLoadout.EpicFight.Spear -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModItems.Spear.WOODEN
                WeaponTier.Stone -> EpicFightModItems.Spear.STONE
                WeaponTier.Golden -> EpicFightModItems.Spear.GOLDEN
                WeaponTier.Iron -> EpicFightModItems.Spear.IRON
                WeaponTier.Diamond -> EpicFightModItems.Spear.DIAMOND
                WeaponTier.Netherite -> EpicFightModItems.Spear.NETHERITE
            }
        ).maybeAddShield(loadout.withShield)

        is WeaponLoadout.EpicFight.Tachi -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModItems.Tachi.WOODEN
                WeaponTier.Stone -> EpicFightModItems.Tachi.STONE
                WeaponTier.Golden -> EpicFightModItems.Tachi.GOLDEN
                WeaponTier.Iron -> EpicFightModItems.Tachi.IRON
                WeaponTier.Diamond -> EpicFightModItems.Tachi.DIAMOND
                WeaponTier.Netherite -> EpicFightModItems.Tachi.NETHERITE
            }
        )

        WeaponLoadout.EpicFight.Uchigatana -> epicFightSingleHand(EpicFightModItems.UCHIGATANA)
        is WeaponLoadout.Vanilla.Axe -> singleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> Items.WOODEN_AXE
                WeaponTier.Stone -> Items.STONE_AXE
                WeaponTier.Golden -> Items.GOLDEN_AXE
                WeaponTier.Iron -> Items.IRON_AXE
                WeaponTier.Diamond -> Items.DIAMOND_AXE
                WeaponTier.Netherite -> Items.NETHERITE_AXE
            }
        )

        WeaponLoadout.Vanilla.Bow -> singleHand(Items.BOW)
        WeaponLoadout.Vanilla.Crossbow -> singleHand(Items.CROSSBOW)
        is WeaponLoadout.Vanilla.Sword -> singleHand(getSwordItemForTier(loadout.tier))
            .maybeAddShield(loadout.withShield)

        is WeaponLoadout.Vanilla.TwoSwords -> dualWield(getSwordItemForTier(loadout.tier))
        WeaponLoadout.Vanilla.Trident -> singleHand(Items.TRIDENT)
    }
}
