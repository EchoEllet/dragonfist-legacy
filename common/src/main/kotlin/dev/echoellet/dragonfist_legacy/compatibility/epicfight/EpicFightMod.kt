package dev.echoellet.dragonfist_legacy.compatibility.epicfight

import dev.echoellet.dragonfist_legacy.compatibility.MinecraftMod
import dev.echoellet.dragonfist_legacy.util.getItemOrThrow
import net.minecraft.resources.ResourceLocation

object EpicFightMod {
    object Items {
        val GLOVE = ItemId("glove")
        val BOKKEN = ItemId("bokken")
        val UCHIGATANA = ItemId("uchigatana")

        object Stray {
            val HAT = ItemId("stray_hat")
            val ROBE = ItemId("stray_robe")
            val PANTS = ItemId("stray_pants")
        }

        object Daggers {
            val WOODEN = ItemId("wooden_dagger")
            val STONE = ItemId("stone_dagger")
            val GOLDEN = ItemId("golden_dagger")
            val IRON = ItemId("iron_dagger")
            val DIAMOND = ItemId("diamond_dagger")
            val NETHERITE = ItemId("netherite_dagger")
        }

        object Longsword {
            val WOODEN = ItemId("wooden_longsword")
            val STONE = ItemId("stone_longsword")
            val GOLDEN = ItemId("golden_longsword")
            val IRON = ItemId("iron_longsword")
            val DIAMOND = ItemId("diamond_longsword")
            val NETHERITE = ItemId("netherite_longsword")
        }

        object GreatSword {
            val WOODEN = ItemId("wooden_greatsword")
            val STONE = ItemId("stone_greatsword")
            val GOLDEN = ItemId("golden_greatsword")
            val IRON = ItemId("iron_greatsword")
            val DIAMOND = ItemId("diamond_greatsword")
            val NETHERITE = ItemId("netherite_greatsword")
        }

        object Spear {
            val WOODEN = ItemId("wooden_spear")
            val STONE = ItemId("stone_spear")
            val GOLDEN = ItemId("golden_spear")
            val IRON = ItemId("iron_spear")
            val DIAMOND = ItemId("diamond_spear")
            val NETHERITE = ItemId("netherite_spear")
        }

        object Tachi {
            val WOODEN = ItemId("wooden_tachi")
            val STONE = ItemId("stone_tachi")
            val GOLDEN = ItemId("golden_tachi")
            val IRON = ItemId("iron_tachi")
            val DIAMOND = ItemId("diamond_tachi")
            val NETHERITE = ItemId("netherite_tachi")
        }
    }

    class ItemId(val id: String) {
        fun asItem(): net.minecraft.world.item.Item {
            return ResourceLocation.fromNamespaceAndPath(
                MinecraftMod.EPIC_FIGHT.modId,
                id
            ).getItemOrThrow()
        }
    }
}
