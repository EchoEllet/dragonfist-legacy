package dev.echoellet.dragonfist_legacy.util

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import kotlin.jvm.optionals.getOrNull

fun ItemStack.hasVanishingCurse(registryAccess: RegistryAccess): Boolean {
    val itemStack = this
    val holder =
        registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolder(Enchantments.VANISHING_CURSE).getOrNull()
            ?: kotlin.run {
                DragonFistLegacy.LOGGER.warn("The ENCHANTMENT registry is not present, which is unexpected.")
                return false
            }
    val hasVanishingCurse = EnchantmentHelper.getTagEnchantmentLevel(holder, itemStack) > 0
    return hasVanishingCurse
}

fun ItemStack.enchanted(key: ResourceKey<Enchantment>, level: Int, registryAccess: RegistryAccess): ItemStack {
    val stack = this

    val mutable = ItemEnchantments.Mutable(ItemEnchantments.EMPTY)
    val holder = registryAccess.registryOrThrow(Registries.ENCHANTMENT).getHolder(key).getOrNull() ?: kotlin.run {
        DragonFistLegacy.LOGGER.error("The ENCHANTMENT registry is not present. Failed to enchant the item: ${stack.displayName}")
        return stack
    }
    mutable.set(holder, level)
    EnchantmentHelper.setEnchantments(stack, mutable.toImmutable())

    return stack
}

object ModResources {
    fun id(path: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(DragonFistLegacy.ID, path)
    }
}
