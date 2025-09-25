package dev.echoellet.dragonfist_legacy.registry.entries.item

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.item.SummonKnightItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.CommonScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.EpicScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.LegendaryScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.RareScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.UncommonScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.tiers.VeryCommonScrollItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.KnightSpawnEggItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit.BanditChampionSpawnEggItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit.BanditEnforcerSpawnEggItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit.BanditRegularSpawnEggItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.ShifuSpawnEggItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit.BanditEliteSpawnEggItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit.BanditLeaderSpawnEggItem
import dev.echoellet.dragonfist_legacy.item.spawn_eggs.bandit.BanditRulerSpawnEggItem
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModItems {
    val REGISTRY: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, DragonFistLegacy.ID)

    private fun <T: Item> registerItem(name: String, builder: () -> T): RegistryObject<T> {
        return REGISTRY.register(name, Supplier {
            builder()
        })
    }

    val VERY_COMMON_SCROLL = registerItem("very_common_scroll") {
        VeryCommonScrollItem()
    }
    val COMMON_SCROLL = registerItem("common_scroll") {
        CommonScrollItem()
    }
    val UNCOMMON_SCROLL = registerItem("uncommon_scroll") {
        UncommonScrollItem()
    }
    val RARE_SCROLL = registerItem("rare_scroll") {
        RareScrollItem()
    }
    val EPIC_SCROLL = registerItem("epic_scroll") {
        EpicScrollItem()
    }
    val LEGENDARY_SCROLL = registerItem("legendary_scroll") {
        LegendaryScrollItem()
    }

    val SHIFU_SPAWN_EGG = registerItem("shifu_spawn_egg") {
        ShifuSpawnEggItem()
    }

    val BANDIT_REGULAR_SPAWN_EGG = registerItem("bandit_regular_spawn_egg") {
        BanditRegularSpawnEggItem()
    }

    val BANDIT_ENFORCER_SPAWN_EGG = registerItem("bandit_enforcer_spawn_egg") {
        BanditEnforcerSpawnEggItem()
    }

    val BANDIT_CHAMPION_SPAWN_EGG = registerItem("bandit_champion_spawn_egg") {
        BanditChampionSpawnEggItem()
    }

    val BANDIT_ELITE_SPAWN_EGG = registerItem("bandit_elite_spawn_egg") {
        BanditEliteSpawnEggItem()
    }

    val BANDIT_LEADER_SPAWN_EGG = registerItem("bandit_leader_spawn_egg") {
        BanditLeaderSpawnEggItem()
    }

    val BANDIT_RULER_SPAWN_EGG = registerItem("bandit_ruler_spawn_egg") {
        BanditRulerSpawnEggItem()
    }

    val KNIGHT_SPAWN_EGG = registerItem("knight_spawn_egg") {
        KnightSpawnEggItem()
    }

    val SUMMON_KNIGHT = registerItem("summon_knight") {
        SummonKnightItem()
    }
}
