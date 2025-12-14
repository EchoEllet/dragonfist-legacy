package dev.echoellet.dragonfist_legacy.registry.entries.item

import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.platform.ModPlatformProvider
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredCreativeModeTab
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

object ModCreativeModeTabs {
    private fun registerTab(
        @Suppress("SameParameterValue")
        name: String,
        builder: () -> CreativeModeTab,
    ): DeferredCreativeModeTab {
        return ModPlatformProvider.get().deferredRegistrar().registerCreativeModeTab(name, builder)
    }

    private val ITEMS: List<Supplier<out Item>> = listOf(
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
        ModItems.SUMMON_KNIGHT,
    )

    lateinit var MOD_ITEMS_TAB: DeferredCreativeModeTab

    fun register() {
        MOD_ITEMS_TAB = registerTab("creative_mode_tab", {
            CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .icon { ItemStack(ModItems.LEGENDARY_SCROLL.get()) }
                .title(Component.translatable(LangKeys.ITEM_GROUP_CREATIVE_MODE_TAB))
                .displayItems { _, tabData ->
                    ITEMS.forEach { item -> tabData.accept(item.get()) }
                }
                .build()
        })
    }
}
