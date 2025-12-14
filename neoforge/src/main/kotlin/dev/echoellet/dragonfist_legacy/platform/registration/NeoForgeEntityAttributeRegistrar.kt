package dev.echoellet.dragonfist_legacy.platform.registration

import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent

class NeoForgeEntityAttributeRegistrar : EntityAttributeRegistrar {
    private val pending = mutableListOf<Pair<DeferredEntity<LivingEntity>, DeferredAttributeSupplier>>()
    private var locked = false

    override fun <T : LivingEntity> register(
        entityType: DeferredEntity<T>,
        attributes: DeferredAttributeSupplier,
    ) {
        require(!locked) {
            "Cannot register entity attributes after the 'EntityAttributeCreationEvent' has fired. " +
                    "All entity attributes must be registered during mod setup, before the event is processed."
        }
        @Suppress("UNCHECKED_CAST")
        (entityType as DeferredEntity<LivingEntity>)
        pending.add(entityType to attributes)
    }

    internal fun onCreation(event: EntityAttributeCreationEvent) {
        for ((entity, builder) in pending) {
            event.put(entity.get(), builder.get().build())
        }
        locked = true
        pending.clear()
    }
}
