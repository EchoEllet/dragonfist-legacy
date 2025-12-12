package dev.echoellet.dragonfist_legacy.entity.knight

import dev.echoellet.dragonfist_legacy.compatibility.MinecraftMod
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponItems
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponItemsResolver
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponLoadout
import dev.echoellet.dragonfist_legacy.entity.common.weapon.WeaponTier
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class KnightWeaponEquipper(private val entity: KnightEntity) {
    companion object {
        private fun getWeaponLoadout(): WeaponLoadout {
            if (!MinecraftMod.EPIC_FIGHT.isLoaded()) {
                return WeaponLoadout.Vanilla.Sword(WeaponTier.Iron, withShield = true)
            }

            return WeaponLoadout.EpicFight.Uchigatana
        }
    }

    fun equipRandomWeapon() {
        val loadout = getWeaponLoadout()
        val loadoutItems = WeaponItemsResolver.resolve(loadout)

        applyWeaponItems(loadoutItems)
    }

    /**
     * Applies the given [WeaponItems] to the knight's hands.
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
