package dev.echoellet.dragonfist_legacy.registry.entries.item

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {
    val REGISTRY: DeferredRegister<CreativeModeTab> = DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB,
        DragonFistLegacy.ID
    )

    private fun registerTab(
        @Suppress("SameParameterValue")
        name: String,
        builder: () -> CreativeModeTab,
    ): Supplier<CreativeModeTab> {
        return REGISTRY.register(name, Supplier { builder() })
    }

    private val ITEMS = listOf(
        ModItems.VERY_COMMON_SCROLL,
        ModItems.COMMON_SCROLL,
        ModItems.UNCOMMON_SCROLL,
        ModItems.RARE_SCROLL,
        ModItems.EPIC_SCROLL,
        ModItems.LEGENDARY_SCROLL,
        ModItems.SHIFU_SPAWN_EGG,
        ModItems.BANDIT_REGULAR_SPAWN_EGG,
        ModItems.BANDIT_ENFORCER_SPAWN_EGG,
        ModItems.BANDIT_CHAMPION_SPAWN_EGG,
        ModItems.BANDIT_ELITE_SPAWN_EGG,
        ModItems.BANDIT_LEADER_SPAWN_EGG,
        ModItems.BANDIT_RULER_SPAWN_EGG,
        ModItems.KNIGHT_SPAWN_EGG,
    )

    @Suppress("unused")
    val MOD_ITEMS_TAB = registerTab("creative_mode_tab", {
        CreativeModeTab.builder()
            .icon { ItemStack(ModItems.LEGENDARY_SCROLL.get()) }
            .title(Component.translatable(LangKeys.ITEM_GROUP_CREATIVE_MODE_TAB))
            .displayItems { _, tabData ->
                ITEMS.forEach { item -> tabData.accept(item) }
            }
            .build()
    })
}
