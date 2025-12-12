package dev.echoellet.dragonfist_legacy.client.platform

import dev.echoellet.dragonfist_legacy.client.platform.registration.EntityRendererRegistrar
import dev.echoellet.dragonfist_legacy.client.platform.registration.NeoForgeEntityRendererRegistrar

class NeoForgeClientModPlatform(private val entityRendererRegistry: NeoForgeEntityRendererRegistrar) :
    ClientModPlatform {
    override fun entityRendererRegistrar(): EntityRendererRegistrar {
        return entityRendererRegistry
    }
}
