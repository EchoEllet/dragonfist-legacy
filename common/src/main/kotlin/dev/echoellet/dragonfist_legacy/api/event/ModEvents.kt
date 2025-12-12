package dev.echoellet.dragonfist_legacy.api.event

import dev.echoellet.dragonfist_legacy.entity.shifu.trade.ShifuScrollTrade
import dev.echoellet.dragonfist_legacy.platform.event.Event
import dev.echoellet.dragonfist_legacy.platform.event.EventHandler
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object ModEvents {
    val SHIFU_TRADE_SUCCESS: EventHandler<ShifuTradeSuccess> = EventHandler.createArrayBacked()

    class ShifuTradeSuccess(
        val player: Player,
        val result: ShifuScrollTrade.TradeResult,
        val level: Level,
        val stackInMainHand: ItemStack,
        val isClientSide: Boolean,
        val isRaining: Boolean
    ) : Event
}