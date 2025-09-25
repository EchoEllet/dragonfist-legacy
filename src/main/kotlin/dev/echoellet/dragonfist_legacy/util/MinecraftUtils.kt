package dev.echoellet.dragonfist_legacy.util

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper

fun ItemStack.hasVanishingCurse(): Boolean {
    return EnchantmentHelper.hasVanishingCurse(this)
}

fun ItemStack.enchanted(enchantment: Enchantment, level: Int): ItemStack {
    val stack = this
    EnchantmentHelper.setEnchantments(
        mapOf(enchantment to level),
        stack
    )
    return stack
}

object ModResources {
    fun id(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(DragonFistLegacy.ID, path)
    }
}
