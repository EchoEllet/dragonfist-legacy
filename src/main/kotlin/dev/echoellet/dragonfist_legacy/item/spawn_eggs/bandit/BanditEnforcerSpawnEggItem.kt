package dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit

import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.entity.bandit.rank.enforcer.BanditEnforcerEntity
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.SpawnEggItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class BanditEnforcerSpawnEggItem: SpawnEggItem<BanditEnforcerEntity>(
    ModEntities.BANDIT_ENFORCER,
    0x654321, // Dark Brown
    0xFFA500, // Orange
) {
    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ITEM_BANDIT_ENFORCER_SPAWN_EGG)
}
