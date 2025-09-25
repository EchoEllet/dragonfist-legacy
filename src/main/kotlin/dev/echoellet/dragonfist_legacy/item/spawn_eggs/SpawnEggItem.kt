package dev.echoellet.dragonfist_legacy.item.spawn_eggs

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.ForgeSpawnEggItem
import java.util.function.Supplier

abstract class SpawnEggItem<T : Mob>(
    type: Supplier<EntityType<T>>,
    backgroundColor: Int,
    highlightColor: Int,
    props: Properties = Properties(),
) : ForgeSpawnEggItem(type, backgroundColor, highlightColor, props) {
    abstract override fun getName(stack: ItemStack): Component
}
