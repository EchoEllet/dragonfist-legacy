package dev.echoellet.dragonfist_legacy.client.platform

import dev.echoellet.dragonfist_legacy.client.platform.registration.EntityRendererRegistrar
import dev.echoellet.dragonfist_legacy.client.platform.registration.FabricEntityRendererRegistrar

class FabricClientModPlatform(private val entityRendererRegistrar: FabricEntityRendererRegistrar) : ClientModPlatform {
    override fun entityRendererRegistrar(): EntityRendererRegistrar {
        return entityRendererRegistrar
    }
}
