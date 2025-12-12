package dev.echoellet.dragonfist_legacy.client.platform

object ClientModPlatformProvider {
    internal var instance: ClientModPlatform? = null
        private set

    internal fun initialize(platform: ClientModPlatform) {
        require(instance == null) { "A client mod platform implementation can be provided only once." }
        instance = platform
    }

    fun get(): ClientModPlatform =
        requireNotNull(instance) { "A client mod platform implementation has not been provided." }
}
