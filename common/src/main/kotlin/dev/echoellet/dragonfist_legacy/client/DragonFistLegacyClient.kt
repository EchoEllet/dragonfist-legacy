package dev.echoellet.dragonfist_legacy.client

import dev.echoellet.dragonfist_legacy.client.entity.ModEntityRenderers
import dev.echoellet.dragonfist_legacy.client.platform.ClientModPlatform
import dev.echoellet.dragonfist_legacy.client.platform.ClientModPlatformProvider

object DragonFistLegacyClient {
    fun initialize(platform: ClientModPlatform) {
        ClientModPlatformProvider.initialize(platform)
        ModEntityRenderers.register()
    }
}