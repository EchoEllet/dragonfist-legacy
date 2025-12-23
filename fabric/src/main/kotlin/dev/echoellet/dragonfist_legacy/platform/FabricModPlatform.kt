package dev.echoellet.dragonfist_legacy.platform

import dev.echoellet.dragonfist_legacy.platform.registration.DeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.EntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.EntitySpawnRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.FabricDeferredRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.FabricEntityAttributeRegistrar
import dev.echoellet.dragonfist_legacy.platform.registration.FabricEntitySpawnRegistrar
import net.fabricmc.loader.api.FabricLoader

class FabricModPlatform(
    private val deferredRegistrar: FabricDeferredRegistrar,
    private val entityAttributeRegistrar: FabricEntityAttributeRegistrar,
    private val entitySpawnRegistrar: FabricEntitySpawnRegistrar,
) : ModPlatform {
    override fun isDevelopmentEnvironment(): Boolean {
        return FabricLoader.getInstance().isDevelopmentEnvironment
    }

    override fun isModLoaded(id: String): Boolean {
        return FabricLoader.getInstance().isModLoaded(id)
    }

    override fun deferredRegistrar(): DeferredRegistrar {
        return deferredRegistrar
    }

    override fun entityAttributeRegistrar(): EntityAttributeRegistrar {
        return entityAttributeRegistrar
    }

    override fun entitySpawnRegistrar(): EntitySpawnRegistrar {
        return entitySpawnRegistrar
    }
}
