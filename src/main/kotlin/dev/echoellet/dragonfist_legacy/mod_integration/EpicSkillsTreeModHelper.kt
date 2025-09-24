package dev.echoellet.dragonfist_legacy.mod_integration

import com.yesman.epicskills.registry.entry.EpicSkillsAttachmentTypes
import com.yesman.epicskills.registry.entry.EpicSkillsSounds
import dev.echoellet.dragonfist_legacy.util.ModLoaderUtils
import net.minecraft.world.entity.player.Player

object EpicSkillsTreeModHelper {
    // https://github.com/Epic-Fight/epicskills/blob/935eb9e7a8a78458e111b4451444dca0d31a1b70/src/main/java/com/yesman/epicskills/world/item/AbilityStoneItem.java#L23-L26
    fun addAbilityPoint(player: Player, pointsToAdd: Int) {
        player.getExistingData(EpicSkillsAttachmentTypes.ABILITY_POINTS).ifPresent { abilityPoints ->
            abilityPoints.abilityPoints += pointsToAdd
            player.playSound(EpicSkillsSounds.GAIN_ABILITY_POINTS.get(), 1.0F, 1.0F)
        }
    }

    fun isInstalled() = ModLoaderUtils.isModInstalled("epicskills")
}
