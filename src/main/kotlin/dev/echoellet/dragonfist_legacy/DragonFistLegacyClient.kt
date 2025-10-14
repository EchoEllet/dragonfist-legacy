package dev.echoellet.dragonfist_legacy

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.gui.ConfigurationScreen
import net.neoforged.neoforge.client.gui.IConfigScreenFactory

@Mod(value = DragonFistLegacy.ID, dist = [Dist.CLIENT])
class DragonFistLegacyClient(container: ModContainer) {
    init {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > mod > config.
        container.registerExtensionPoint<IConfigScreenFactory>(
            IConfigScreenFactory::class.java,
            IConfigScreenFactory { mod, parent -> ConfigurationScreen(mod, parent) }
        )
    }
}
