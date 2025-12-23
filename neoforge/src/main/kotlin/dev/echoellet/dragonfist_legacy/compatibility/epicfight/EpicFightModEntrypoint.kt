package dev.echoellet.dragonfist_legacy.compatibility.epicfight

import dev.echoellet.dragonfist_legacy.api.event.ModEvents
import dev.echoellet.dragonfist_legacy.compatibility.IntegrationEntrypoint
import dev.echoellet.dragonfist_legacy.util.enchanted
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantments

class EpicFightModEntrypoint : IntegrationEntrypoint {
    override fun onInitialize() {
        ModEvents.Shifu.ON_FINALIZE_SPAWN.register {
            val shifu = it.entity
            equipEpicFightGloves(shifu)
        }
    }

    private fun equipEpicFightGloves(entity: LivingEntity) {
        val level = entity.level()
        val gloveItem: Item = EpicFightModItems.GLOVE.asItem()

        fun gloveStack(): ItemStack {
            val stack = ItemStack(gloveItem)
            val level = level

            val sharpnessLevel = when (level.difficulty == Difficulty.HARD) {
                true -> 5
                false -> 4
            }
            return stack.enchanted(Enchantments.SHARPNESS, sharpnessLevel, level.registryAccess())
        }

        entity.setItemSlot(EquipmentSlot.MAINHAND, gloveStack())
        entity.setItemSlot(EquipmentSlot.OFFHAND, gloveStack())
    }
}
