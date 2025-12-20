package dev.echoellet.dragonfist_legacy.util

import dev.echoellet.dragonfist_legacy.compatibility.mca.MCARebornMod
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.npc.AbstractVillager

fun LivingEntity.isVillagerEntity(): Boolean {
    val entity = this
    // Never attacks Villagers from MCA reborn mod, same as vanilla villagers.
    return entity is AbstractVillager || (MCARebornMod.isVillager(entity))
}
