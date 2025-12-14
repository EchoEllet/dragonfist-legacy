package dev.echoellet.dragonfist_legacy.client.platform.registration

import dev.echoellet.dragonfist_legacy.platform.registration.DeferredEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.Entity
import net.neoforged.neoforge.client.event.EntityRenderersEvent

class NeoForgeEntityRendererRegistrar : EntityRendererRegistrar {
    private data class PendingEntry<T : Entity>(
        val entity: DeferredEntity<T>,
        val renderer: EntityRendererProvider<T>,
    )

    private val pending = mutableListOf<PendingEntry<Entity>>()
    private var locked = false

    override fun <T : Entity> register(
        entityType: DeferredEntity<T>,
        renderer: EntityRendererProvider<T>
    ) {
        require(!locked) {
            "Cannot register entity renderers after the 'EntityRenderersEvent.RegisterRenderers' has fired. " +
                    "All entity renderers must be registered during mod setup, before the event is processed."
        }
        @Suppress("UNCHECKED_CAST")
        (entityType as DeferredEntity<Entity>)
        @Suppress("UNCHECKED_CAST")
        (renderer as EntityRendererProvider<Entity>)
        pending.add(PendingEntry(entityType, renderer))
    }

    internal fun onRegistration(event: EntityRenderersEvent.RegisterRenderers) {
        for ((entity, builder) in pending) {
            val entityType = entity.get()
            event.registerEntityRenderer(
                entityType,
            ) { context ->
                val value = builder.create(context)
                requireNotNull(value) {
                    """
                    Failed to create an EntityRenderer for '${entityType::class.simpleName}'.
                    Expected renderer of type 'EntityRenderer<${entityType::class.simpleName}>',
                    but got '${value.let { it::class.simpleName } ?: "null"}'.
                """.trimIndent()
                }
            }
        }
        locked = true
        pending.clear()
    }
}
