package dev.echoellet.dragonfist_legacy.platform

import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.EntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.EntitySpawnRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.NeoForgeDeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.NeoForgeEntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.NeoForgeEntitySpawnRegistrar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.neoforged.fml.ModList
import net.neoforged.neoforge.common.DeferredSpawnEggItem

class NeoForgeModPlatform(
    private val deferredRegistrar: NeoForgeDeferredRegistrar,
    private val entityAttributeRegistrar: NeoForgeEntityAttributeRegistrar,
    private val entitySpawnRegistrar: NeoForgeEntitySpawnRegistrar,
) : ModPlatform {
    override fun isModLoaded(id: String): Boolean {
        return ModList.get().isLoaded(id)
    }

    override fun deferredRegistrar(): DeferredRegistrar {
        return deferredRegistrar
    }

    override fun entityAttributeRegistrar(): EntityAttributeRegistrar {
        return entityAttributeRegistrar
    }

    override fun entitySpawnRegistrar(): EntitySpawnRegistrar {
        return entitySpawnRegistrar
    }

    override fun <T : Mob> createSpawnEggItem(
        type: DeferredEntity<T>,
        backgroundColor: Int,
        highlightColor: Int,
        displayName: Component,
        props: Item.Properties,
    ): SpawnEggItem {
        return object : DeferredSpawnEggItem(type, backgroundColor, highlightColor, props) {
            override fun getName(stack: ItemStack): Component {
                return displayName
            }
        }
    }
}
