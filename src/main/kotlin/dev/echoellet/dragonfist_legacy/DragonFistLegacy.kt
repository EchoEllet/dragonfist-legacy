package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.registry.ModRegistries
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS as KOTLIN_MOD_BUS

@Mod(DragonFistLegacy.ID)
class DragonFistLegacy(modEventBus: IEventBus, modContainer: ModContainer) {
    companion object {
        const val ID = "dragonfist_legacy"

        val LOGGER: Logger = LogManager.getLogger(ID)

        /**
         * IMPORTANT: Avoid using the hardcoded [net.neoforged.neoforge.common.NeoForge.EVENT_BUS].
         * Since this mod is loaded via Kotlin for Forge, their [KOTLIN_MOD_BUS] must be used instead.
         *
         * Preferably, use [modEventBus], which already points to the correct bus.
         */
        private val MOD_BUS = KOTLIN_MOD_BUS
    }

    init {
        ModRegistries.registerAll(modEventBus)
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SPEC)
    }
}
