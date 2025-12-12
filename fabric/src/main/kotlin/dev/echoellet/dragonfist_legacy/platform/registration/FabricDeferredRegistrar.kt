package dev.echoellet.dragonfist_legacy.platform.registration

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import java.util.function.Supplier

class FabricDeferredRegistrar : DeferredRegistrar {
    override fun <T : Item> registerItem(
        name: String,
        builder: () -> T,
    ): DeferredItem<T> {
        return register(BuiltInRegistries.ITEM, name, builder())
    }

    override fun <T : Entity> registerEntity(
        name: String,
        builder: EntityType.Builder<T>
    ): DeferredEntity<T> {
        return register(BuiltInRegistries.ENTITY_TYPE, name, builder.build(name))
    }

    override fun registerSound(name: String): DeferredSoundEvent {
        val location = DragonFistLegacy.rl(name)
        return register(BuiltInRegistries.SOUND_EVENT, name, SoundEvent.createVariableRangeEvent(location))
    }

    override fun registerCreativeModeTab(
        name: String,
        builder: () -> CreativeModeTab
    ): DeferredCreativeModeTab {
        return register(BuiltInRegistries.CREATIVE_MODE_TAB, name, builder())
    }

    private fun <R : Any, T : R> register(
        registry: Registry<R>,
        name: String,
        value: T,
    ): Supplier<T> {
        val registered = Registry.register(registry, DragonFistLegacy.rl(name), value)
        return Supplier { registered }
    }
}
