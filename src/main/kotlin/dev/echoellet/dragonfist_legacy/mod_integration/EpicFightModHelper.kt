package dev.echoellet.dragonfist_legacy.mod_integration

import dev.echoellet.dragonfist_legacy.util.ModLoaderUtils
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.SwordItem
import net.neoforged.neoforge.registries.DeferredHolder
import yesman.epicfight.registry.entries.EpicFightItems
import yesman.epicfight.world.item.DaggerItem
import yesman.epicfight.world.item.GloveItem
import yesman.epicfight.world.item.GreatswordItem
import yesman.epicfight.world.item.LongswordItem
import yesman.epicfight.world.item.SpearItem
import yesman.epicfight.world.item.TachiItem
import yesman.epicfight.world.item.UchigatanaItem

object EpicFightModHelper {
    private val isEpicFightModInstalled by lazy { ModLoaderUtils.isModInstalled("epicfight") }
    fun isInstalled() = isEpicFightModInstalled

    object Items {
        val GLOVE: DeferredHolder<Item, GloveItem> = EpicFightItems.GLOVE
        val BOKKEN: DeferredHolder<Item, SwordItem> = EpicFightItems.BOKKEN
        val UCHIGATANA: DeferredHolder<Item, UchigatanaItem> = EpicFightItems.UCHIGATANA

        object Stray {
            val HAT: DeferredHolder<Item, ArmorItem> = EpicFightItems.STRAY_HAT
            val ROBE: DeferredHolder<Item, ArmorItem> = EpicFightItems.STRAY_ROBE
            val PANTS: DeferredHolder<Item, ArmorItem> = EpicFightItems.STRAY_PANTS
        }

        object Daggers {
            val WOODEN: DeferredHolder<Item, DaggerItem> = EpicFightItems.WOODEN_DAGGER
            val STONE: DeferredHolder<Item, DaggerItem> = EpicFightItems.STONE_DAGGER
            val GOLDEN: DeferredHolder<Item, DaggerItem> = EpicFightItems.GOLDEN_DAGGER
            val IRON: DeferredHolder<Item, DaggerItem> = EpicFightItems.IRON_DAGGER
            val DIAMOND: DeferredHolder<Item, DaggerItem> = EpicFightItems.DIAMOND_DAGGER
            val NETHERITE: DeferredHolder<Item, DaggerItem> = EpicFightItems.NETHERITE_DAGGER
        }

        object Longsword {
            val WOODEN: DeferredHolder<Item, LongswordItem> = EpicFightItems.WOODEN_LONGSWORD
            val STONE: DeferredHolder<Item, LongswordItem> = EpicFightItems.STONE_LONGSWORD
            val GOLDEN: DeferredHolder<Item, LongswordItem> = EpicFightItems.GOLDEN_LONGSWORD
            val IRON: DeferredHolder<Item, LongswordItem> = EpicFightItems.IRON_LONGSWORD
            val DIAMOND: DeferredHolder<Item, LongswordItem> = EpicFightItems.DIAMOND_LONGSWORD
            val NETHERITE: DeferredHolder<Item, LongswordItem> = EpicFightItems.NETHERITE_LONGSWORD
        }

        object GreatSword {
            val WOODEN: DeferredHolder<Item, GreatswordItem> = EpicFightItems.WOODEN_GREATSWORD
            val STONE: DeferredHolder<Item, GreatswordItem> = EpicFightItems.STONE_GREATSWORD
            val GOLDEN: DeferredHolder<Item, GreatswordItem> = EpicFightItems.GOLDEN_GREATSWORD
            val IRON: DeferredHolder<Item, GreatswordItem> = EpicFightItems.IRON_GREATSWORD
            val DIAMOND: DeferredHolder<Item, GreatswordItem> = EpicFightItems.DIAMOND_GREATSWORD
            val NETHERITE: DeferredHolder<Item, GreatswordItem> = EpicFightItems.NETHERITE_GREATSWORD
        }

        object Spear {
            val WOODEN: DeferredHolder<Item, SpearItem> = EpicFightItems.WOODEN_SPEAR
            val STONE: DeferredHolder<Item, SpearItem> = EpicFightItems.STONE_SPEAR
            val GOLDEN: DeferredHolder<Item, SpearItem> = EpicFightItems.GOLDEN_SPEAR
            val IRON: DeferredHolder<Item, SpearItem> = EpicFightItems.IRON_SPEAR
            val DIAMOND: DeferredHolder<Item, SpearItem> = EpicFightItems.DIAMOND_SPEAR
            val NETHERITE: DeferredHolder<Item, SpearItem> = EpicFightItems.NETHERITE_SPEAR
        }

        object Tachi {
            val WOODEN: DeferredHolder<Item, TachiItem> = EpicFightItems.WOODEN_TACHI
            val STONE: DeferredHolder<Item, TachiItem> = EpicFightItems.STONE_TACHI
            val GOLDEN: DeferredHolder<Item, TachiItem> = EpicFightItems.GOLDEN_TACHI
            val IRON: DeferredHolder<Item, TachiItem> = EpicFightItems.IRON_TACHI
            val DIAMOND: DeferredHolder<Item, TachiItem> = EpicFightItems.DIAMOND_TACHI
            val NETHERITE: DeferredHolder<Item, TachiItem> = EpicFightItems.NETHERITE_TACHI
        }
    }
}
