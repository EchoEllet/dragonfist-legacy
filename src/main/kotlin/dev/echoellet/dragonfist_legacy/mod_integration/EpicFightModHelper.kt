package dev.echoellet.dragonfist_legacy.mod_integration

import dev.echoellet.dragonfist_legacy.util.ModLoaderUtils
import net.minecraft.world.item.Item
import net.minecraftforge.registries.RegistryObject
import yesman.epicfight.world.item.EpicFightItems

object EpicFightModHelper {
    private val isEpicFightModInstalled by lazy { ModLoaderUtils.isModInstalled("epicfight") }
    fun isInstalled() = isEpicFightModInstalled

    object Items {
        val GLOVE: RegistryObject<Item> = EpicFightItems.GLOVE
        val BOKKEN: RegistryObject<Item> = EpicFightItems.BOKKEN
        val UCHIGATANA: RegistryObject<Item> = EpicFightItems.UCHIGATANA

        object Stray {
            val HAT: RegistryObject<Item> = EpicFightItems.STRAY_HAT
            val ROBE: RegistryObject<Item> = EpicFightItems.STRAY_ROBE
            val PANTS: RegistryObject<Item> = EpicFightItems.STRAY_PANTS
        }

        object Daggers {
            val WOODEN: RegistryObject<Item> = EpicFightItems.WOODEN_DAGGER
            val STONE: RegistryObject<Item> = EpicFightItems.STONE_DAGGER
            val GOLDEN: RegistryObject<Item> = EpicFightItems.GOLDEN_DAGGER
            val IRON: RegistryObject<Item> = EpicFightItems.IRON_DAGGER
            val DIAMOND: RegistryObject<Item> = EpicFightItems.DIAMOND_DAGGER
            val NETHERITE: RegistryObject<Item> = EpicFightItems.NETHERITE_DAGGER
        }

        object Longsword {
            val WOODEN: RegistryObject<Item> = EpicFightItems.WOODEN_LONGSWORD
            val STONE: RegistryObject<Item> = EpicFightItems.STONE_LONGSWORD
            val GOLDEN: RegistryObject<Item> = EpicFightItems.GOLDEN_LONGSWORD
            val IRON: RegistryObject<Item> = EpicFightItems.IRON_LONGSWORD
            val DIAMOND: RegistryObject<Item> = EpicFightItems.DIAMOND_LONGSWORD
            val NETHERITE: RegistryObject<Item> = EpicFightItems.NETHERITE_LONGSWORD
        }

        object GreatSword {
            val WOODEN: RegistryObject<Item> = EpicFightItems.WOODEN_GREATSWORD
            val STONE: RegistryObject<Item> = EpicFightItems.STONE_GREATSWORD
            val GOLDEN: RegistryObject<Item> = EpicFightItems.GOLDEN_GREATSWORD
            val IRON: RegistryObject<Item> = EpicFightItems.IRON_GREATSWORD
            val DIAMOND: RegistryObject<Item> = EpicFightItems.DIAMOND_GREATSWORD
            val NETHERITE: RegistryObject<Item> = EpicFightItems.NETHERITE_GREATSWORD
        }

        object Spear {
            val WOODEN: RegistryObject<Item> = EpicFightItems.WOODEN_SPEAR
            val STONE: RegistryObject<Item> = EpicFightItems.STONE_SPEAR
            val GOLDEN: RegistryObject<Item> = EpicFightItems.GOLDEN_SPEAR
            val IRON: RegistryObject<Item> = EpicFightItems.IRON_SPEAR
            val DIAMOND: RegistryObject<Item> = EpicFightItems.DIAMOND_SPEAR
            val NETHERITE: RegistryObject<Item> = EpicFightItems.NETHERITE_SPEAR
        }

        object Tachi {
            val WOODEN: RegistryObject<Item> = EpicFightItems.WOODEN_TACHI
            val STONE: RegistryObject<Item> = EpicFightItems.STONE_TACHI
            val GOLDEN: RegistryObject<Item> = EpicFightItems.GOLDEN_TACHI
            val IRON: RegistryObject<Item> = EpicFightItems.IRON_TACHI
            val DIAMOND: RegistryObject<Item> = EpicFightItems.DIAMOND_TACHI
            val NETHERITE: RegistryObject<Item> = EpicFightItems.NETHERITE_TACHI
        }
    }
}
