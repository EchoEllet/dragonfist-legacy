package dev.echoellet.dragonfist_legacy.registry.entries.item

import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.SummonKnightItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.CommonScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.EpicScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.LegendaryScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.RareScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.UncommonScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.VeryCommonScrollItem
import dev.echoellet.dragonfist_legacy.platform.ModPlatformProvider
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredItem
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.item.SpawnEggItem

object ModItems {
    private fun <T : Item> registerItem(name: String, builder: () -> T): DeferredItem<T> {
        return ModPlatformProvider.get().deferredRegistrar().registerItem(name, builder)
    }

    private fun <T : Mob> registerSpawnEggItem(
        name: String,
        type: DeferredEntity<T>,
        backgroundColor: Int,
        highlightColor: Int,
        displayName: Component,
        props: Item.Properties = Item.Properties(),
    ): DeferredItem<SpawnEggItem> {
        return registerItem(name) {
            ModPlatformProvider.get().createSpawnEggItem(
                type, backgroundColor, highlightColor, displayName, props,
            )
        }
    }

    lateinit var VERY_COMMON_SCROLL: DeferredItem<VeryCommonScrollItem>
    lateinit var COMMON_SCROLL: DeferredItem<CommonScrollItem>
    lateinit var UNCOMMON_SCROLL: DeferredItem<UncommonScrollItem>
    lateinit var RARE_SCROLL: DeferredItem<RareScrollItem>
    lateinit var EPIC_SCROLL: DeferredItem<EpicScrollItem>
    lateinit var LEGENDARY_SCROLL: DeferredItem<LegendaryScrollItem>

    lateinit var SHIFU_SPAWN_EGG: DeferredItem<SpawnEggItem>
    lateinit var BANDIT_REGULAR_SPAWN_EGG: DeferredItem<SpawnEggItem>
    lateinit var BANDIT_ENFORCER_SPAWN_EGG: DeferredItem<SpawnEggItem>
    lateinit var BANDIT_CHAMPION_SPAWN_EGG: DeferredItem<SpawnEggItem>
    lateinit var BANDIT_ELITE_SPAWN_EGG: DeferredItem<SpawnEggItem>
    lateinit var BANDIT_LEADER_SPAWN_EGG: DeferredItem<SpawnEggItem>

    lateinit var BANDIT_RULER_SPAWN_EGG: DeferredItem<SpawnEggItem>

    lateinit var KNIGHT_SPAWN_EGG: DeferredItem<SpawnEggItem>
    lateinit var SUMMON_KNIGHT: DeferredItem<SummonKnightItem>

    fun register() {
        VERY_COMMON_SCROLL = registerItem("very_common_scroll") { VeryCommonScrollItem() }
        COMMON_SCROLL = registerItem("common_scroll") { CommonScrollItem() }
        UNCOMMON_SCROLL = registerItem("uncommon_scroll") { UncommonScrollItem() }
        RARE_SCROLL = registerItem("rare_scroll") { RareScrollItem() }
        EPIC_SCROLL = registerItem("epic_scroll") { EpicScrollItem() }
        LEGENDARY_SCROLL = registerItem("legendary_scroll") { LegendaryScrollItem() }

        SHIFU_SPAWN_EGG = registerSpawnEggItem(
            "shifu_spawn_egg",
            ModEntities.SHIFU,
            0x2E2E2E, // Primary: deep black/gray
            0xD4AF37, // Secondary: gold
            Component.translatable(LangKeys.ITEM_SHIFU_SPAWN_EGG),
        )
        BANDIT_REGULAR_SPAWN_EGG = registerSpawnEggItem(
            "bandit_regular_spawn_egg",
            ModEntities.BANDIT_REGULAR,
            0x8B4513, // SaddleBrown
            0xFFD700, // Gold
            Component.translatable(LangKeys.ITEM_BANDIT_REGULAR_SPAWN_EGG)
        )
        BANDIT_ENFORCER_SPAWN_EGG = registerSpawnEggItem(
            "bandit_enforcer_spawn_egg",
            ModEntities.BANDIT_ENFORCER,
            0x654321, // Dark Brown
            0xFFA500, // Orange
            Component.translatable(LangKeys.ITEM_BANDIT_ENFORCER_SPAWN_EGG)
        )
        BANDIT_CHAMPION_SPAWN_EGG = registerSpawnEggItem(
            "bandit_champion_spawn_egg",
            ModEntities.BANDIT_CHAMPION,
            0x4B0082, // Indigo
            0xFF4500, // OrangeRed
            Component.translatable(LangKeys.ITEM_BANDIT_CHAMPION_SPAWN_EGG),
        )
        BANDIT_ELITE_SPAWN_EGG = registerSpawnEggItem(
            "bandit_elite_spawn_egg",
            ModEntities.BANDIT_ELITE,
            0x2F4F4F, // Dark Slate Gray
            0x00CED1, // Dark Turquoise
            Component.translatable(LangKeys.ITEM_BANDIT_ELITE_SPAWN_EGG)
        )

        BANDIT_LEADER_SPAWN_EGG = registerSpawnEggItem(
            "bandit_leader_spawn_egg",
            ModEntities.BANDIT_LEADER,
            0x000080, // Navy
            0xFFFF00, // Yellow
            Component.translatable(LangKeys.ITEM_BANDIT_LEADER_SPAWN_EGG)
        )

        BANDIT_RULER_SPAWN_EGG = registerSpawnEggItem(
            "bandit_ruler_spawn_egg",
            ModEntities.BANDIT_RULER,
            0x800000, // Maroon
            0xFFD700, // Gold
            Component.translatable(LangKeys.ITEM_BANDIT_RULER_SPAWN_EGG)
        )
        KNIGHT_SPAWN_EGG = registerSpawnEggItem(
            "knight_spawn_egg",
            ModEntities.KNIGHT,
            0x555555, // Dark gray
            0xAAAAAA, // Light gray/silver highlights
            Component.translatable(LangKeys.ENTITY_KNIGHT_SPAWN_EGG)
        )
        SUMMON_KNIGHT = registerItem("summon_knight") { SummonKnightItem() }
    }
}
