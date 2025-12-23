package dev.echoellet.dragonfist_legacy.compatibility.epicskills

import com.yesman.epicskills.registry.entry.EpicSkillsAttachmentTypes
import com.yesman.epicskills.registry.entry.EpicSkillsSounds
import dev.echoellet.dragonfist_legacy.api.event.ModEvents
import dev.echoellet.dragonfist_legacy.compatibility.IntegrationEntrypoint
import net.minecraft.world.entity.player.Player

class EpicSkillsModEntrypoint : IntegrationEntrypoint {
    override fun onInitialize() {
        ModEvents.Shifu.TRADE_SUCCESS.register { event ->
            addAbilityPoint(event.player, pointsToAdd = event.result.abilityPointsGained)
        }
    }

    companion object {
        // https://github.com/Epic-Fight/epicskills/blob/935eb9e7a8a78458e111b4451444dca0d31a1b70/src/main/java/com/yesman/epicskills/world/item/AbilityStoneItem.java#L23-L26
        private fun addAbilityPoint(player: Player, pointsToAdd: Int) {
            player.getExistingData(EpicSkillsAttachmentTypes.ABILITY_POINTS).ifPresent { abilityPoints ->
                abilityPoints.abilityPoints += pointsToAdd
                player.playSound(EpicSkillsSounds.GAIN_ABILITY_POINTS.get(), 1.0F, 1.0F)
            }
        }
    }
}
