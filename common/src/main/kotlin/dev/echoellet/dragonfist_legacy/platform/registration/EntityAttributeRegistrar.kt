package dev.echoellet.dragonfist_legacy.platform.registration

import net.minecraft.world.entity.LivingEntity

interface EntityAttributeRegistrar {
    fun <T : LivingEntity> register(
        entityType: DeferredEntity<T>,
        attributes: DeferredAttributeSupplier,
    )
}
