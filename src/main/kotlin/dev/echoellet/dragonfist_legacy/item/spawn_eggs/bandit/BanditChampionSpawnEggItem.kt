package dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit

import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.champion.BanditChampionEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.SpawnEggItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class BanditChampionSpawnEggItem : SpawnEggItem<BanditChampionEntity>(
    ModEntities.BANDIT_CHAMPION,
    0x4B0082, // Indigo
    0xFF4500, // OrangeRed
) {
    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ITEM_BANDIT_CHAMPION_SPAWN_EGG)
}
