package dev.echoellet.dragonfist_legacy.mod_integration

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType

object MCARebornModHelper {
    // https://github.com/Luke100000/minecraft-comes-alive/blob/3011fcd6e5e1f6cf3b0244030ce6ad886d14216c/common/src/main/java/net/mca/entity/EntitiesMCA.java#L26-L33

    private const val MALE_VILLAGER_ID = "male_villager"
    private const val FEMALE_VILLAGER = "female_villager"

    fun isVillager(entity: Entity): Boolean {
        val id = EntityType.getKey(entity.type).path ?: false

        return id == MALE_VILLAGER_ID || id == FEMALE_VILLAGER
    }
}
