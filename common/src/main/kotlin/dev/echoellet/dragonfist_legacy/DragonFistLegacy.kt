package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.platform.ModPlatform
import dev.echoellet.dragonfist_legacy.platform.ModPlatformProvider
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntityAttributes
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntitySpawnRestrictions
import dev.echoellet.dragonfist_legacy.registry.entries.item.ModCreativeModeTabs
import dev.echoellet.dragonfist_legacy.registry.entries.item.ModItems
import dev.echoellet.dragonfist_legacy.registry.entries.sound.ModSoundEvents
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object DragonFistLegacy {
    const val ID = "dragonfist_legacy"
    val LOGGER: Logger = LogManager.getLogger(ID)

    fun rl(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(ID, path)

    fun initialize(modPlatform: ModPlatform) {
        ModPlatformProvider.initialize(modPlatform)

        // Some entries, for example, creative mode tabs, may reference items.
        // The registration must be in the exact correct order, due to "lateinit var".
        ModEntities.register()
        ModEntityAttributes.register()
        ModEntitySpawnRestrictions.register()
        ModSoundEvents.register()
        ModItems.register()
        ModCreativeModeTabs.register()
    }
}
