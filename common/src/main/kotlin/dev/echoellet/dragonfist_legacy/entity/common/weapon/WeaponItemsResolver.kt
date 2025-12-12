package dev.echoellet.dragonfist_legacy.entity.common.weapon

import dev.echoellet.dragonfist_legacy.compatibility.epicfight.EpicFightMod
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
    private fun epicFightDualWield(itemId: EpicFightMod.ItemId): WeaponItems {
        val item = itemId.asItem()
        return WeaponItems(item, item)
    }

    private fun epicFightSingleHand(itemId: EpicFightMod.ItemId): WeaponItems =
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
        WeaponLoadout.EpicFight.TwoBokken -> epicFightDualWield(EpicFightMod.Items.BOKKEN)
        is WeaponLoadout.EpicFight.Daggers -> epicFightDualWield(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightMod.Items.Daggers.WOODEN
                WeaponTier.Stone -> EpicFightMod.Items.Daggers.STONE
                WeaponTier.Golden -> EpicFightMod.Items.Daggers.GOLDEN
                WeaponTier.Iron -> EpicFightMod.Items.Daggers.IRON
                WeaponTier.Diamond -> EpicFightMod.Items.Daggers.DIAMOND
                WeaponTier.Netherite -> EpicFightMod.Items.Daggers.NETHERITE
            }
        )

        WeaponLoadout.EpicFight.Gloves -> epicFightDualWield(EpicFightMod.Items.GLOVE)
        is WeaponLoadout.EpicFight.Greatsword -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightMod.Items.GreatSword.WOODEN
                WeaponTier.Stone -> EpicFightMod.Items.GreatSword.STONE
                WeaponTier.Golden -> EpicFightMod.Items.GreatSword.GOLDEN
                WeaponTier.Iron -> EpicFightMod.Items.GreatSword.IRON
                WeaponTier.Diamond -> EpicFightMod.Items.GreatSword.DIAMOND
                WeaponTier.Netherite -> EpicFightMod.Items.GreatSword.NETHERITE
            }
        )

        is WeaponLoadout.EpicFight.Longsword -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightMod.Items.Longsword.WOODEN
                WeaponTier.Stone -> EpicFightMod.Items.Longsword.STONE
                WeaponTier.Golden -> EpicFightMod.Items.Longsword.GOLDEN
                WeaponTier.Iron -> EpicFightMod.Items.Longsword.IRON
                WeaponTier.Diamond -> EpicFightMod.Items.Longsword.DIAMOND
                WeaponTier.Netherite -> EpicFightMod.Items.Longsword.NETHERITE
            }
        ).maybeAddShield(loadout.withShield)

        is WeaponLoadout.EpicFight.Spear -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightMod.Items.Spear.WOODEN
                WeaponTier.Stone -> EpicFightMod.Items.Spear.STONE
                WeaponTier.Golden -> EpicFightMod.Items.Spear.GOLDEN
                WeaponTier.Iron -> EpicFightMod.Items.Spear.IRON
                WeaponTier.Diamond -> EpicFightMod.Items.Spear.DIAMOND
                WeaponTier.Netherite -> EpicFightMod.Items.Spear.NETHERITE
            }
        ).maybeAddShield(loadout.withShield)

        is WeaponLoadout.EpicFight.Tachi -> epicFightSingleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightMod.Items.Tachi.WOODEN
                WeaponTier.Stone -> EpicFightMod.Items.Tachi.STONE
                WeaponTier.Golden -> EpicFightMod.Items.Tachi.GOLDEN
                WeaponTier.Iron -> EpicFightMod.Items.Tachi.IRON
                WeaponTier.Diamond -> EpicFightMod.Items.Tachi.DIAMOND
                WeaponTier.Netherite -> EpicFightMod.Items.Tachi.NETHERITE
            }
        )

        WeaponLoadout.EpicFight.Uchigatana -> epicFightSingleHand(EpicFightMod.Items.UCHIGATANA)
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
