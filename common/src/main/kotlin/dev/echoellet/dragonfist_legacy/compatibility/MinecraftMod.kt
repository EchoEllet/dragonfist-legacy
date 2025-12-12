package dev.echoellet.dragonfist_legacy.compatibility

import dev.echoellet.dragonfist_legacy.platform.ModPlatformProvider

enum class MinecraftMod(val modId: String) {
    EPIC_FIGHT("epicfight"),
    EPIC_SKILLS("epicskills"),
    ;

    fun isLoaded(): Boolean {
        return ModPlatformProvider.get().isModLoaded(modId)
    }
}
