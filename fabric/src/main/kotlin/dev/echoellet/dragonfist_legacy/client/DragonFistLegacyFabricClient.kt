package dev.echoellet.dragonfist_legacy.client

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.client.platform.FabricClientModPlatform
import dev.echoellet.dragonfist_legacy.client.platform.registration.FabricEntityRendererRegistrar
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.client.ConfigScreenFactoryRegistry
import net.fabricmc.api.ClientModInitializer
import net.neoforged.neoforge.client.gui.ConfigurationScreen

object DragonFistLegacyFabricClient : ClientModInitializer {
    override fun onInitializeClient() {
        DragonFistLegacyClient.initialize(
            FabricClientModPlatform(
                entityRendererRegistrar = FabricEntityRendererRegistrar(),
            )
        )
        ConfigScreenFactoryRegistry.INSTANCE.register(DragonFistLegacy.ID, ::ConfigurationScreen)
    }
}
