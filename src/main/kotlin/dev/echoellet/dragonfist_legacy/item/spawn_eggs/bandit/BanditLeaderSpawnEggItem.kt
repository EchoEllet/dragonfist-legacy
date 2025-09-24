package dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit

import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader.BanditLeaderEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.SpawnEggItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class BanditLeaderSpawnEggItem : SpawnEggItem<BanditLeaderEntity>(
    ModEntities.BANDIT_LEADER,
    0x000080, // Navy
    0xFFFF00, // Yellow
) {
    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ITEM_BANDIT_LEADER_SPAWN_EGG)
}
