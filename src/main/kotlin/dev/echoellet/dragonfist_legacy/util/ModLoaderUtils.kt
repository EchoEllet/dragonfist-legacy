package dev.echoellet.dragonfist_legacy.util

import net.neoforged.fml.ModList

object ModLoaderUtils {
    fun isModInstalled(modId: String) = ModList.get().isLoaded(modId)
}
