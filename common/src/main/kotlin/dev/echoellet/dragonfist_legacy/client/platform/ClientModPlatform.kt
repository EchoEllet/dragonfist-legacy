package dev.echoellet.dragonfist_legacy.client.platform

import dev.echoellet.dragonfist_legacy.client.platform.registration.EntityRendererRegistrar

interface ClientModPlatform {
    fun entityRendererRegistrar(): EntityRendererRegistrar
}
