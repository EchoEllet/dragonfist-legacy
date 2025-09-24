package dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit

import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.elite.BanditEliteEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.SpawnEggItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class BanditEliteSpawnEggItem : SpawnEggItem<BanditEliteEntity>(
    ModEntities.BANDIT_ELITE,
    0x2F4F4F, // Dark Slate Gray
    0x00CED1, // Dark Turquoise
    Properties(),
) {
    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ITEM_BANDIT_ELITE_SPAWN_EGG)
}
