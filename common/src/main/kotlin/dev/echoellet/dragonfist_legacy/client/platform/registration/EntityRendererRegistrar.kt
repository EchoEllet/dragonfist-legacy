package dev.echoellet.dragonfist_legacy.client.platform.registration

import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity

interface EntityRendererRegistrar {
    fun <T : Entity> register(
        entityType: DeferredEntity<T>,
        renderer: EntityRendererProvider<T>
    )
}
