package dev.echoellet.dragonfist_legacy.platform.registration

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

class NeoForgeDeferredRegistrar(
    private val creativeTab: DeferredRegister<CreativeModeTab>,
    private val item: DeferredRegister.Items,
    private val entity: DeferredRegister<EntityType<*>>,
    private val sound: DeferredRegister<SoundEvent>,
) : DeferredRegistrar {
    override fun <T : Item> registerItem(
        name: String,
        builder: () -> T
    ): DeferredItem<T> {
        return item.register(name, Supplier {
            builder()
        })
    }

    override fun <T : Entity> registerEntity(
        name: String,
        builder: EntityType.Builder<T>
    ): DeferredEntity<T> {
        return entity.register(name, Supplier { builder.build(name) })
    }

    override fun registerSound(name: String): DeferredSoundEvent {
        val location = DragonFistLegacy.identifier(name)
        return sound.register(
            name,
            Supplier { SoundEvent.createVariableRangeEvent(location) }
        )
    }

    override fun registerCreativeModeTab(
        name: String,
        builder: () -> CreativeModeTab
    ): DeferredCreativeModeTab {
        return creativeTab.register(name, Supplier { builder() })
    }
}
