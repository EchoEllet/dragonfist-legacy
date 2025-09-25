package dev.echoellet.dragonfist_legacy.util

import net.minecraftforge.fml.ModList

object ModLoaderUtils {
    fun isModInstalled(modId: String) = ModList.get().isLoaded(modId)
}
