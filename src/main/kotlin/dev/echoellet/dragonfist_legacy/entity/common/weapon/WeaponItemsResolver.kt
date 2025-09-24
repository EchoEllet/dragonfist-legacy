package dev.echoellet.dragonfist_legacy.entity.common.weapon

import dev.echoellet.dragonfist_legacy.mod_integration.EpicFightModHelper
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.registries.DeferredHolder

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
    sealed interface Vanilla: WeaponLoadout {
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
    sealed interface EpicFight: WeaponLoadout {
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
    private fun dualWield(item: DeferredHolder<out Item, *>): WeaponItems =
        WeaponItems(item.get(), item.get())

    private fun singleHand(item: DeferredHolder<out Item, *>): WeaponItems =
        WeaponItems(item.get(), null)

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
        WeaponLoadout.EpicFight.TwoBokken -> dualWield(EpicFightModHelper.Items.BOKKEN)
        is WeaponLoadout.EpicFight.Daggers -> dualWield(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModHelper.Items.Daggers.WOODEN
                WeaponTier.Stone -> EpicFightModHelper.Items.Daggers.STONE
                WeaponTier.Golden -> EpicFightModHelper.Items.Daggers.GOLDEN
                WeaponTier.Iron -> EpicFightModHelper.Items.Daggers.IRON
                WeaponTier.Diamond -> EpicFightModHelper.Items.Daggers.DIAMOND
                WeaponTier.Netherite -> EpicFightModHelper.Items.Daggers.NETHERITE
            }
        )

        WeaponLoadout.EpicFight.Gloves -> dualWield(EpicFightModHelper.Items.GLOVE)
        is WeaponLoadout.EpicFight.Greatsword -> singleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModHelper.Items.GreatSword.WOODEN
                WeaponTier.Stone -> EpicFightModHelper.Items.GreatSword.STONE
                WeaponTier.Golden -> EpicFightModHelper.Items.GreatSword.GOLDEN
                WeaponTier.Iron -> EpicFightModHelper.Items.GreatSword.IRON
                WeaponTier.Diamond -> EpicFightModHelper.Items.GreatSword.DIAMOND
                WeaponTier.Netherite -> EpicFightModHelper.Items.GreatSword.NETHERITE
            }
        )

        is WeaponLoadout.EpicFight.Longsword -> singleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModHelper.Items.Longsword.WOODEN
                WeaponTier.Stone -> EpicFightModHelper.Items.Longsword.STONE
                WeaponTier.Golden -> EpicFightModHelper.Items.Longsword.GOLDEN
                WeaponTier.Iron -> EpicFightModHelper.Items.Longsword.IRON
                WeaponTier.Diamond -> EpicFightModHelper.Items.Longsword.DIAMOND
                WeaponTier.Netherite -> EpicFightModHelper.Items.Longsword.NETHERITE
            }
        ).maybeAddShield(loadout.withShield)

        is WeaponLoadout.EpicFight.Spear -> singleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModHelper.Items.Spear.WOODEN
                WeaponTier.Stone -> EpicFightModHelper.Items.Spear.STONE
                WeaponTier.Golden -> EpicFightModHelper.Items.Spear.GOLDEN
                WeaponTier.Iron -> EpicFightModHelper.Items.Spear.IRON
                WeaponTier.Diamond -> EpicFightModHelper.Items.Spear.DIAMOND
                WeaponTier.Netherite -> EpicFightModHelper.Items.Spear.NETHERITE
            }
        ).maybeAddShield(loadout.withShield)

        is WeaponLoadout.EpicFight.Tachi -> singleHand(
            when (loadout.tier) {
                WeaponTier.Wooden -> EpicFightModHelper.Items.Tachi.WOODEN
                WeaponTier.Stone -> EpicFightModHelper.Items.Tachi.STONE
                WeaponTier.Golden -> EpicFightModHelper.Items.Tachi.GOLDEN
                WeaponTier.Iron -> EpicFightModHelper.Items.Tachi.IRON
                WeaponTier.Diamond -> EpicFightModHelper.Items.Tachi.DIAMOND
                WeaponTier.Netherite -> EpicFightModHelper.Items.Tachi.NETHERITE
            }
        )

        WeaponLoadout.EpicFight.Uchigatana -> singleHand(EpicFightModHelper.Items.UCHIGATANA)
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
