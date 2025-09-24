package dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit

import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.regular.BanditRegularEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.SpawnEggItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class BanditRegularSpawnEggItem : SpawnEggItem<BanditRegularEntity>(
    ModEntities.BANDIT_REGULAR,
    0x8B4513, // SaddleBrown
    0xFFD700, // Gold
) {
    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ITEM_BANDIT_REGULAR_SPAWN_EGG)
}
