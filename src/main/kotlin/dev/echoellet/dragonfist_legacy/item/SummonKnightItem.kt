package dev.echoellet.dragonfist_legacy.item

import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.registry.entries.entity.ModEntities
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent

class SummonKnightItem : Item(
    Properties().fireResistant().rarity(Rarity.UNCOMMON)
) {
    override fun appendHoverText(
        stack: ItemStack,
        level: Level?,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag)

        tooltipComponents.add(Component.translatable(LangKeys.TOOLTIP_SUMMON_KNIGHT))
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val blockPos = context.clickedPos
        val direction = context.clickedFace
        val blockState = level.getBlockState(blockPos)
        val spawnPos = if (blockState.isAir) blockPos else blockPos.relative(direction)

        if (level.isClientSide) {
            level.playLocalSound(
                spawnPos.x + 0.5,
                spawnPos.y + 0.5,
                spawnPos.z + 0.5,
                SoundEvents.IRON_GOLEM_STEP,
                SoundSource.PLAYERS,
                1.0f,
                1.0f,
                false,
            )

            repeat(8) {
                level.addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    spawnPos.x + 0.5 + level.random.nextDouble() * 0.4 - 0.15,
                    spawnPos.y + 2.2 + level.random.nextDouble() * 0.3,
                    spawnPos.z + 0.5 + level.random.nextDouble() * 0.25 - 0.15,
                    0.0, 0.05, 0.0
                )
            }
            return InteractionResult.SUCCESS
        }

        val player = context.player
        val stack = context.itemInHand

        // Similar to spawn egg item
        val entity = ModEntities.KNIGHT.get().spawn(
            level as ServerLevel,
            stack,
            player,
            spawnPos,
            MobSpawnType.MOB_SUMMONED,
            true,
            direction == Direction.UP && spawnPos != blockPos
        )

        if (entity != null) {
            stack.shrink(1)
            level.gameEvent(player, GameEvent.ENTITY_PLACE, spawnPos)
        }

        return InteractionResult.CONSUME
    }

    override fun getName(stack: ItemStack): Component = Component.translatable(LangKeys.ITEM_SUMMON_KNIGHT)
}
