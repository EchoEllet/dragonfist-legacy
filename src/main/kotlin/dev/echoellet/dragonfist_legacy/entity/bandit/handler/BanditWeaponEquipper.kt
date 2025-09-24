package dev.echoellet.dragonfist_legacy.entity.bandit.handler

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditEntity
import dev.echoellet.dragonfist_legacy.entity.bandit.handler.BanditWeaponEquipper.Companion.getAvailableLoadoutsForRank
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.BanditRank
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponItems
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponItemsResolver
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponLoadout
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponTier
import dev.echoellet.dragonfist_legacy.mod_integration.EpicFightModHelper
import dev.echoellet.dragonfist_legacy.util.minecraftRandom
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

/**
 * Handles equipping a [BanditEntity] with weapons according to its rank.
 *
 * Supports vanilla Minecraft weapons and Epic Fight mod loadouts if the mod is installed.
 */
class BanditWeaponEquipper(private val entity: BanditEntity) {
    companion object {
        /**
         * Returns a merged list of weapon loadouts.
         *
         * - Includes EpicFight loadouts if the mod is installed.
         * - Includes vanilla loadouts if provided.
         *
         * @param vanilla Optional list of vanilla loadouts.
         * @param epicFight Optional list of Epic Fight loadouts.
         * @param excludeVanillaIfEpicFightIsInstalled Whether to exclude the list of [vanilla] loadouts if Epic Fight is installed.
         * @return Combined list of available loadouts.
         */
        private fun combineAvailableLoadouts(
            vanilla: List<WeaponLoadout>? = null,
            epicFight: List<WeaponLoadout>? = null,
            excludeVanillaIfEpicFightIsInstalled: Boolean = false
        ): List<WeaponLoadout> = buildList {
            val isEpicFightInstalled = EpicFightModHelper.isInstalled()
            if (isEpicFightInstalled) {
                epicFight?.let { addAll(it) }
            }
            vanilla?.let {
                if (excludeVanillaIfEpicFightIsInstalled && isEpicFightInstalled) {
                    return@let
                }
                addAll(it)
            }
        }

        /**
         * Returns all possible weapon loadouts for a given [BanditRank].
         *
         * Lower ranks get weaker or mixed loadouts.
         * Higher ranks receive stronger and more guaranteed weapons.
         *
         * @param rank The rank of the bandit.
         * @return List of [WeaponLoadout] available for this rank.
         */
        private fun getAvailableLoadoutsForRank(rank: BanditRank): List<WeaponLoadout> = when (rank) {
            BanditRank.Regular -> combineAvailableLoadouts(
                epicFight = listOf(
                    WeaponLoadout.EpicFight.Gloves,
                    WeaponLoadout.EpicFight.TwoBokken,
                    WeaponLoadout.EpicFight.Daggers(WeaponTier.Stone),
                    WeaponLoadout.EpicFight.Spear(WeaponTier.Wooden),
                    WeaponLoadout.EpicFight.Greatsword(WeaponTier.Wooden),
                    WeaponLoadout.EpicFight.Longsword(WeaponTier.Wooden),
                    WeaponLoadout.EpicFight.Tachi(WeaponTier.Wooden),
                ),
                vanilla = listOf(
                    WeaponLoadout.Vanilla.Axe(WeaponTier.Wooden),
                    WeaponLoadout.Vanilla.Sword(WeaponTier.Wooden),
                    WeaponLoadout.Vanilla.TwoSwords(WeaponTier.Wooden),
                )
            )

            BanditRank.Enforcer -> combineAvailableLoadouts(
                epicFight = listOf(
                    WeaponLoadout.EpicFight.Daggers(WeaponTier.Iron),
                    WeaponLoadout.EpicFight.Spear(WeaponTier.Iron),
                    WeaponLoadout.EpicFight.Greatsword(WeaponTier.Stone),
                    WeaponLoadout.EpicFight.Longsword(WeaponTier.Iron),
                    WeaponLoadout.EpicFight.Tachi(WeaponTier.Stone),
                    WeaponLoadout.EpicFight.Uchigatana,
                ),
                vanilla = listOf(
                    WeaponLoadout.Vanilla.Axe(WeaponTier.Iron),
                    WeaponLoadout.Vanilla.TwoSwords(WeaponTier.Iron),
                    WeaponLoadout.Vanilla.Sword(WeaponTier.Iron, true),
                    WeaponLoadout.Vanilla.Trident,
                )
            )

            BanditRank.Champion -> combineAvailableLoadouts(
                epicFight = listOf(
                    WeaponLoadout.EpicFight.Tachi(WeaponTier.Iron),
                    WeaponLoadout.EpicFight.Longsword(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Greatsword(WeaponTier.Iron),
                    WeaponLoadout.EpicFight.Daggers(WeaponTier.Diamond),
                ),
                vanilla = listOf(
                    WeaponLoadout.Vanilla.Axe(WeaponTier.Iron),
                    WeaponLoadout.Vanilla.Sword(WeaponTier.Iron, true),
                    WeaponLoadout.Vanilla.TwoSwords(WeaponTier.Iron),
                ),
                excludeVanillaIfEpicFightIsInstalled = true,
            )

            BanditRank.Elite -> combineAvailableLoadouts(
                epicFight = listOf(
                    WeaponLoadout.EpicFight.Tachi(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Longsword(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Greatsword(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Spear(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Uchigatana,
                ),
                vanilla = listOf(
                    WeaponLoadout.Vanilla.Sword(WeaponTier.Diamond, true),
                    WeaponLoadout.Vanilla.TwoSwords(WeaponTier.Iron),
                    WeaponLoadout.Vanilla.Trident,
                    WeaponLoadout.Vanilla.Axe(WeaponTier.Netherite),
                ),
                excludeVanillaIfEpicFightIsInstalled = true,
            )

            BanditRank.Leader -> combineAvailableLoadouts(
                epicFight = listOf(
                    WeaponLoadout.EpicFight.Tachi(WeaponTier.Netherite),
                    WeaponLoadout.EpicFight.Longsword(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Greatsword(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Spear(WeaponTier.Diamond),
                    WeaponLoadout.EpicFight.Uchigatana,
                ),
                vanilla = listOf(
                    WeaponLoadout.Vanilla.Sword(WeaponTier.Netherite, true),
                    WeaponLoadout.Vanilla.TwoSwords(WeaponTier.Diamond),
                    WeaponLoadout.Vanilla.Axe(WeaponTier.Netherite),
                ),
                excludeVanillaIfEpicFightIsInstalled = true,
            )

            BanditRank.Ruler -> combineAvailableLoadouts(
                epicFight = listOf(
                    WeaponLoadout.EpicFight.Tachi(WeaponTier.Netherite),
                    WeaponLoadout.EpicFight.Longsword(WeaponTier.Netherite),
                    WeaponLoadout.EpicFight.Greatsword(WeaponTier.Netherite),
                    WeaponLoadout.EpicFight.Spear(WeaponTier.Netherite),
                    WeaponLoadout.EpicFight.Uchigatana,
                ),
                vanilla = listOf(
                    WeaponLoadout.Vanilla.Sword(WeaponTier.Netherite, true),
                    WeaponLoadout.Vanilla.TwoSwords(WeaponTier.Netherite),
                    WeaponLoadout.Vanilla.Axe(WeaponTier.Netherite),
                ),
                excludeVanillaIfEpicFightIsInstalled = true,
            )
        }
    }

    /**
     * Equips the bandit with a random weapon loadout based on its rank.
     *
     * Selects one loadout from the list returned by [getAvailableLoadoutsForRank]
     * and resolves it into actual [WeaponItems] to equip.
     *
     * Call this once during spawn to assign weapons.
     */
    fun equipRandomWeapon() {
        val banditRank = entity.getRank()
        val randomLoadout = getAvailableLoadoutsForRank(banditRank).minecraftRandom(entity.random)
        val weaponLoadoutItems = WeaponItemsResolver.resolve(randomLoadout)

        applyWeaponItems(weaponLoadoutItems)
    }


    /**
     * Applies the given [WeaponItems] to the bandit's hands.
     *
     * Sets the main hand and offhand items if present.
     *
     * @param items The resolved weapons to equip.
     */
    private fun applyWeaponItems(items: WeaponItems) {
        items.mainHand?.let { entity.setItemSlot(EquipmentSlot.MAINHAND, createWeaponItemStack(it)) }
        items.offhand?.let { entity.setItemSlot(EquipmentSlot.OFFHAND, createWeaponItemStack(it)) }
    }

    /**
     * Creates an [ItemStack] for the given weapon [item].
     *
     * Currently, returns a plain [ItemStack] without enchantments or modifications, but that's subject to change.
     *
     * @param item The weapon [Item] to convert.
     * @return An [ItemStack] ready to equip.
     */
    private fun createWeaponItemStack(item: Item): ItemStack {
        val itemStack = ItemStack(item)
        return itemStack
    }
}
