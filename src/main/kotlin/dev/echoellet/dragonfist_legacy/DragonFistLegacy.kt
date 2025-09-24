package dev.echoellet.dragonfist_legacy

import dev.echoellet.dragonfist_legacy.registry.ModRegistries
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
// IMPORTANT: Use thedarkcolour.kotlinforforge.forge.MOD_BUS instead of
// net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Mod(DragonFistLegacy.ID)
@EventBusSubscriber
object DragonFistLegacy {
    const val ID = "dragonfist_legacy"

    val LOGGER: Logger = LogManager.getLogger(ID)

    init {
        LOGGER.info("Registering items, entities, and sounds...")

        // IMPORTANT: Use thedarkcolour.kotlinforforge.forge.MOD_BUS instead of net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
        ModRegistries.registerAll(MOD_BUS)

        runForDist(clientTarget = {
            MOD_BUS.addListener(::onClientSetup)
        }, serverTarget = {
            MOD_BUS.addListener(::onServerSetup)
        })
    }

    /**
     * Client specific setup, such as renders and keymaps.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.debug("Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
        LOGGER.debug("Server starting...")
    }

    @SubscribeEvent
    fun onCommonSetup(event: FMLCommonSetupEvent) {
        LOGGER.debug("Common setup complete.")
    }
}
