package dev.echoellet.dragonfist_legacy.item.spawn_eggs

import dev.echoellet.dragonfist_legacy.entity.knight.KnightEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class KnightSpawnEggItem: SpawnEggItem<KnightEntity>(
    ModEntities.KNIGHT,
    0x555555, // Dark gray
    0xAAAAAA // Light gray/silver highlights
) {
    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ENTITY_KNIGHT_SPAWN_EGG)
}
