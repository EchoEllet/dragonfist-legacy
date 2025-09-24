package dev.echoellet.dragonfist_legacy.item.spawn_eggs

import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class ShifuSpawnEggItem : SpawnEggItem<ShifuEntity>(
    ModEntities.SHIFU,
    0x2E2E2E, // Primary: deep black/gray
    0xD4AF37, // Secondary: gold
) {
    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ITEM_SHIFU_SPAWN_EGG)
}
