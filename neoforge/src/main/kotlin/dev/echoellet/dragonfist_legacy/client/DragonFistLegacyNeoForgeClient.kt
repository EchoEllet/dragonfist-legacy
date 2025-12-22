package dev.echoellet.dragonfist_legacy.client

import dev.echoellet.dragonfist_legacy.client.platform.NeoForgeClientModPlatform
import dev.echoellet.dragonfist_legacy.client.platform.registration.NeoForgeEntityRendererRegistrar
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory

// Note: We tried using "@Mod(DragonFistLegacy.ID, Dist.CLIENT)",
// however, in production side, the client entrypoint is called
// before the common entrypoint, which is not an issue during development.
//
// Kotlin "lateinit var" is intentionally used, and client renderers try to access
// entities, which are initialized in the common entrypoint.
object DragonFistLegacyNeoForgeClient {
    fun initialize(modEventBus: IEventBus, container: ModContainer) {
        DragonFistLegacyClient.initialize(
            NeoForgeClientModPlatform(
                entityRendererRegistry = NeoForgeEntityRendererRegistrar().also {
                    modEventBus.addListener<EntityRenderersEvent.RegisterRenderers> { event -> it.onRegistration(event) }
                }
            )
        )
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > mod > config.
        container.registerExtensionPoint(
            IConfigScreenFactory::class.java,
            IConfigScreenFactory { mod, parent -> ConfigurationScreen(mod, parent) }
        )
    }
}
