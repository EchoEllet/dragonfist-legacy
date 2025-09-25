package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.registry.ModRegistries
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.MOD_BUS as KOTLIN_MOD_BUS

@Mod(DragonFistLegacy.ID)
class DragonFistLegacy {
    companion object {
        const val ID = "dragonfist_legacy"

        val LOGGER: Logger = LogManager.getLogger(ID)

        /**
         * IMPORTANT: Avoid using the hardcoded [MinecraftForge.EVENT_BUS].
         * Since this mod is loaded via Kotlin for Forge, their [KOTLIN_MOD_BUS] must be used instead.
         */
        private val MOD_BUS = KOTLIN_MOD_BUS
    }

    init {
        ModRegistries.registerAll(MOD_BUS)
        @Suppress("DEPRECATION", "removal")
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SPEC)
    }
}
