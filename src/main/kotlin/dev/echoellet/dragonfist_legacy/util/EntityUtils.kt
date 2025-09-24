package dev.echoellet.dragonfist_legacy.util

import dev.echoellet.dragonfist_legacy.mod_integration.MCARebornModHelper
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.npc.Villager

fun LivingEntity.isVillagerEntity(): Boolean {
    val entity = this
    // Never attacks Villagers from MCA reborn mod, same as vanilla villagers.
    return entity is Villager || (MCARebornModHelper.isVillager(entity))
}
