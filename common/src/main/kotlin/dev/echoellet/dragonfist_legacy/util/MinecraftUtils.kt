package dev.echoellet.dragonfist_legacy.util

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.ItemEnchantments
import kotlin.jvm.optionals.getOrNull

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

fun Identifier.getItemOrThrow(): Item {
    return BuiltInRegistries.ITEM.get(this)
}

// Prepares for 1.21.11: https://neoforged.net/news/21.11release/#renaming-of-resourcelocation-to-identifier
typealias Identifier = ResourceLocation

fun namespaceIdentifier(namespace: String, path: String): Identifier {
    return Identifier.fromNamespaceAndPath(namespace, path)
}
