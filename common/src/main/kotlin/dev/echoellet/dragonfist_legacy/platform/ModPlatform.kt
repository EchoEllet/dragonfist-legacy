package dev.echoellet.dragonfist_legacy.platform

import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import dev.echoellet.dragonfist_legacy.platform.registration.DeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.EntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.EntitySpawnRegistrar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem

interface ModPlatform {
    fun isDevelopmentEnvironment(): Boolean

    fun isModLoaded(id: String): Boolean

    fun deferredRegistrar(): DeferredRegistrar

    fun entityAttributeRegistrar(): EntityAttributeRegistrar

    fun entitySpawnRegistrar(): EntitySpawnRegistrar

    fun <T : Mob> createSpawnEggItem(
        type: DeferredEntity<T>,
        backgroundColor: Int,
        highlightColor: Int,
        displayName: Component,
        props: Item.Properties,
    ): SpawnEggItem {
        return object : SpawnEggItem(
            type.get(),
            backgroundColor,
            highlightColor,
            props,
        ) {
            override fun getName(stack: ItemStack): Component {
                return displayName
            }
        }
    }
}
