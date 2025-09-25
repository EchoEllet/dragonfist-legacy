package dev.echoellet.dragonfist_legacy.mod_integration

import com.yesman.epicskills.registry.entry.EpicSkillsSounds
import com.yesman.epicskills.world.capability.AbilityPoints
import dev.echoellet.dragonfist_legacy.util.ModLoaderUtils
import net.minecraft.world.entity.player.Player

object EpicSkillsTreeModHelper {
    // https://github.com/Epic-Fight/epicskills/blob/1.20.1/src/main/java/com/yesman/epicskills/world/item/AbilityStoneItem.java#L23-L26
    fun addAbilityPoint(player: Player, pointsToAdd: Int) {
        player.getCapability(AbilityPoints.ABILITY_POINTS).ifPresent { abilityPoints ->
            abilityPoints.abilityPoints += pointsToAdd
            player.playSound(EpicSkillsSounds.GAIN_ABILITY_POINTS.get(), 1.0F, 1.0F)
        }
    }

    fun isInstalled() = ModLoaderUtils.isModInstalled("epicskills")
}
