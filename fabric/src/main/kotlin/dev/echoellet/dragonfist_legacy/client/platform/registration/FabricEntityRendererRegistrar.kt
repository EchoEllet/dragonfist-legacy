package dev.echoellet.dragonfist_legacy.client.platform.registration

import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry as FabricEntityRendererRegistry

class FabricEntityRendererRegistrar : EntityRendererRegistrar {
    override fun <T : Entity> register(
        entityType: DeferredEntity<T>,
        renderer: EntityRendererProvider<T>
    ) {
        FabricEntityRendererRegistry.register(
            entityType.get()
        ) { context -> renderer.create(context) as EntityRenderer<T> }
    }
}
