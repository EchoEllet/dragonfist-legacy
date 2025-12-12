package dev.echoellet.dragonfist_legacy.platform

object ModPlatformProvider {
    internal var instance: ModPlatform? = null
        private set

    internal fun initialize(platform: ModPlatform) {
        require(instance == null) { "A mod platform implementation can be provided only once." }
        instance = platform
    }

    fun get(): ModPlatform =
        requireNotNull(instance) { "A mod platform implementation has not been provided." }
}
