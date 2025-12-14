package dev.echoellet.dragonfist_legacy.platform.registration

import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import java.util.function.Supplier

typealias DeferredObject<T> = Supplier<T>

typealias DeferredCreativeModeTab = DeferredObject<CreativeModeTab>
typealias DeferredItem<T> = DeferredObject<T>
typealias DeferredEntity<T> = DeferredObject<EntityType<T>>
typealias DeferredSoundEvent = DeferredObject<SoundEvent>
typealias DeferredAttributeSupplier = DeferredObject<AttributeSupplier.Builder>

interface DeferredRegistrar {
    fun <T : Item> registerItem(name: String, builder: () -> T): DeferredItem<T>

    fun <T : Entity> registerEntity(
        name: String,
        builder: EntityType.Builder<T>
    ): DeferredEntity<T>

    fun registerSound(name: String): DeferredSoundEvent

    fun registerCreativeModeTab(
        name: String,
        builder: () -> CreativeModeTab,
    ): DeferredCreativeModeTab
}
