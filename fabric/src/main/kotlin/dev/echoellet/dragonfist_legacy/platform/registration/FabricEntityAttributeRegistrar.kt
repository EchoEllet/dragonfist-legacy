package dev.echoellet.dragonfist_legacy.platform.registration

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.world.entity.LivingEntity

class FabricEntityAttributeRegistrar : EntityAttributeRegistrar {
    override fun <T : LivingEntity> register(
        entityType: DeferredEntity<T>,
        attributes: DeferredAttributeSupplier
    ) {
        FabricDefaultAttributeRegistry.register(entityType.get(), attributes.get().build())
    }
}
