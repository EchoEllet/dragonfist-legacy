package dev.echoellet.dragonfist_legacy.entity.shifu.handlers

import dev.echoellet.dragonfist_legacy.api.event.ModEvents
import dev.echoellet.dragonfist_legacy.compatibility.MinecraftMod
import dev.echoellet.dragonfist_legacy.entity.shifu.ShifuEntity
import dev.echoellet.dragonfist_legacy.entity.shifu.trade.ShifuScrollTrade
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageKeys
import dev.echoellet.dragonfist_legacy.entity.shifu.util.ShifuMessageUtils
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollItem
import dev.echoellet.dragonfist_legacy.util.constants.Constants
import dev.echoellet.dragonfist_legacy.util.getRandomFocusSeconds
import net.minecraft.ChatFormatting
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ShifuInteractionHandler(private val entity: ShifuEntity) {

    private fun successResult(isClientSide: Boolean) = InteractionResult.sidedSuccess(isClientSide)
    private val serverSuccessResult = successResult(false)
    private val clientSuccessResult = successResult(true)

    private fun isAcceptableItemInMainHand(stack: ItemStack) = (stack.isEmpty || stack.item is ScrollItem)

    fun handle(player: Player, hand: InteractionHand): InteractionResult {
        val acceptedHand = InteractionHand.MAIN_HAND

        if (hand != acceptedHand) {
            return InteractionResult.PASS
        }

        val stackInMainHand = player.getItemInHand(acceptedHand)

        val level = player.level()
        val isClientSide = level.isClientSide
        val isRaining = level.isRaining

        val target = entity.target
        val item = stackInMainHand.item

        if (!isClientSide && target != null) {
            // FYI: Target is always null on the client side.
            return handleWhileCombat(player, target, stackInMainHand)
        }

        if (stackInMainHand.isEmpty) {
            return handleTalk(
                player = player,
                isRaining = isRaining,
                isClientSide = isClientSide,
            )
        }

        if (item !is ScrollItem) {
            if (!isClientSide) {
                // IMPORTANT: Use an action bar message (true) instead of a chat message.
                // The Uchigatana item from Epic Fight, when used with the Sword Guard skill,
                // can trigger frequent interaction events while Shifu is not in combat mode.
                // Using the action bar avoids chat spam by replacing the message.
                player.displayClientMessage(
                    Component.translatable(LangKeys.ACTIONBAR_SHIFU_EMPTY_HANDS_REQUIRED),
                    true
                )
            }
            // If the hands are not empty and the item is not a scroll item, then keep interactions minimal
            // and ask player to use empty hands using actin bar (avoid chat message)
            // to avoid unpredictable side effects when using modded items, such as spam or bugs.
            return InteractionResult.PASS
        }

        if (!MinecraftMod.EPIC_SKILLS.isLoaded()) {
            return if (isClientSide) clientSuccessResult else handleEpicSkillsAddonNotInstalled(player)
        }

        val tier = item.tier

        val tradeResult = ShifuScrollTrade.tryTrade(
            tier = tier,
            playerScrolls = stackInMainHand.count
        )

        if (tradeResult == null) {
            // Player does not have enough scrolls for at least one ability point.
            return handleNotEnoughScrolls(
                isClientSide = isClientSide,
                isRaining = isRaining,
                player = player
            )
        }

        return handleSuccessfulTrade(
            player = player,
            result = tradeResult,
            level = level,
            stackInMainHand = stackInMainHand,
            isClientSide = isClientSide,
            isRaining = isRaining
        )
    }

    /**
     * Handles player interaction with Shifu on a successful trade.
     *
     * This method must be called **on both the server and the client**.
     * Returns [successResult] to stop further interaction processing (other hand).
     */
    private fun handleSuccessfulTrade(
        player: Player,
        result: ShifuScrollTrade.TradeResult,
        level: Level,
        stackInMainHand: ItemStack,
        isClientSide: Boolean,
        isRaining: Boolean
    ): InteractionResult {
        // BUG: Calling addAbilityPoint() and addParticle() runs only on the client side (temporary, not persisted)
        // when attempting to trade during combat with Shifu. On the client, Shifu's target is null, and only the server
        // has the correct state. This is a known issue, but fixing it adds little value, so it is accepted as-is.

        ModEvents.Shifu.TRADE_SUCCESS.invoke(
            ModEvents.Shifu.TradeSuccess(
                player, result, level, stackInMainHand, isClientSide, isRaining
            )
        )

        if (isClientSide) {
            level.addParticle(
                ParticleTypes.HAPPY_VILLAGER,
                entity.x, entity.y + 2.2, entity.z,
                0.0, 0.0, 0.0
            )
            return clientSuccessResult
        }

        stackInMainHand.shrink(result.scrollsConsumed)
        val messageKey = getRandomExchangeSuccessMessageKey(isRaining)

        player.displayClientMessage(
            Component.translatable(messageKey),
            false
        )
        entity.focusOnPlayer(player, getSuccessFocusSeconds())
        player.swing(InteractionHand.MAIN_HAND, true)

        return serverSuccessResult
    }

    /**
     * Handles player interaction with Shifu while in combat with the player or another target.
     *
     * This method must be called **only on the server**. The client must never call this,
     * since Shifu's target is always unknown on the client.
     *
     * Accepts only empty hand or ScrollItems. Any other item is ignored.
     * Returns [serverSuccessResult] to stop further interaction processing (other hand).
     */
    private fun handleWhileCombat(
        player: Player,
        target: LivingEntity,
        stackInMainHand: ItemStack
    ): InteractionResult {
        if (!isAcceptableItemInMainHand(stackInMainHand)) {
            // Accept only empty hand and scroll items to avoid letting the
            // Sword Guard (Epic Fight Skill) triggering and spamming the chat.
            return InteractionResult.PASS
        }

        val messageKey = when (target == player) {
            true -> ShifuMessageKeys.EXCHANGE_DURING_FIGHT
            false -> ShifuMessageKeys.BUSY
        }

        player.displayClientMessage(
            Component.translatable(messageKey),
            false
        )

        return serverSuccessResult
    }

    /**
     * Handles player interaction with Shifu when the player’s main hand is empty.
     *
     * Empty-hand interaction is reserved for initiating dialogue (talking) with Shifu.
     *
     * This method must be called **on both the server and the client**.
     * Returns [successResult] to stop further interaction processing (other hand).
     */
    private fun handleTalk(
        player: Player,
        isRaining: Boolean,
        isClientSide: Boolean,
    ): InteractionResult {
        if (!isClientSide) {
            val messageKey = getRandomTalkMessageKey(isRaining)

            player.displayClientMessage(Component.translatable(messageKey), false)
            entity.focusOnPlayer(player, getRejectionFocusSeconds())

            player.displayClientMessage(
                Component.translatable(LangKeys.ACTIONBAR_SCROLLS_HUNT_BANDITS_FOR_SCROLLS),
                true
            )
        }

        return successResult(isClientSide)
    }

    /**
     * Handles player interaction with Shifu when the item in main hand is scrolls but
     * the mod "Epic Fight: Skill Tree" is not installed.
     *
     * This method must be called **only on the server**. The client must never call this.
     * Returns [serverSuccessResult] to stop further interaction processing (other hand).
     */
    private fun handleEpicSkillsAddonNotInstalled(player: Player): InteractionResult {
        player.displayClientMessage(
            Component.translatable(ShifuMessageKeys.PATIENCE_BEFORE_LEARNING),
            false
        )
        entity.focusOnPlayer(player, getRejectionFocusSeconds())

        val styledMessage = Component.translatable(LangKeys.MESSAGE_SYSTEM_INSTALL_SKILLS_ADDON)
            .copy()
            .withStyle(ChatFormatting.RED)
            .append(
                Component.literal(" ") // leading space
                    .append(
                        Component.translatable(LangKeys.ACTIONBAR_CLICK_TO_INSTALL)
                            .withStyle(
                                Style.EMPTY
                                    .withColor(ChatFormatting.AQUA)
                                    .withUnderlined(true)
                                    .withClickEvent(
                                        ClickEvent(
                                            ClickEvent.Action.OPEN_URL,
                                            Constants.EPIC_FIGHT_SKILL_TREE_ADDON_INSTALL_LINK
                                        )
                                    )
                            )
                    )
            )

        player.displayClientMessage(
            styledMessage,
            false
        )

        return serverSuccessResult
    }

    /**
     * Handles player interaction with Shifu scrolls are not enough for at least one ability point.
     *
     * This method must be called **on both the server and the client**.
     * Returns [successResult] to stop further interaction processing (other hand).
     */
    private fun handleNotEnoughScrolls(
        isClientSide: Boolean,
        isRaining: Boolean,
        player: Player
    ): InteractionResult {
        if (!isClientSide) {
            val messageKey = getRandomNotEnoughScrollsMessageKey(isRaining)

            player.displayClientMessage(Component.translatable(messageKey), false)
            player.displayClientMessage(
                Component.translatable(LangKeys.ACTIONBAR_SCROLLS_NOT_ENOUGH),
                true
            )

            entity.focusOnPlayer(player, getRejectionFocusSeconds())
        }

        return successResult(isClientSide)
    }

    companion object {
        private const val MIN_REJECTION_FOCUS_SECONDS = 2
        private const val MAX_REJECTION_FOCUS_SECONDS = 4
        private const val MIN_SUCCESS_FOCUS_SECONDS = 3
        private const val MAX_SUCCESS_FOCUS_SECONDS = 5
    }

    private fun getRejectionFocusSeconds() =
        entity.random.getRandomFocusSeconds(MIN_REJECTION_FOCUS_SECONDS, MAX_REJECTION_FOCUS_SECONDS)

    private fun getSuccessFocusSeconds() =
        entity.random.getRandomFocusSeconds(MIN_SUCCESS_FOCUS_SECONDS, MAX_SUCCESS_FOCUS_SECONDS)

    private fun getRandomExchangeSuccessMessageKey(isRaining: Boolean): String =
        ShifuMessageUtils.getRandomMessageKey(
            entity.random,
            ShifuMessageKeys.ExchangeSuccess.NORMAL,
            ShifuMessageKeys.ExchangeSuccess.RAIN,
            isRaining
        )

    private fun getRandomTalkMessageKey(isRaining: Boolean): String =
        ShifuMessageUtils.getRandomMessageKey(
            entity.random,
            ShifuMessageKeys.Talk.NORMAL,
            ShifuMessageKeys.Talk.RAIN,
            isRaining
        )

    private fun getRandomNotEnoughScrollsMessageKey(isRaining: Boolean): String =
        ShifuMessageUtils.getRandomMessageKey(
            entity.random,
            ShifuMessageKeys.NotEnoughScrolls.NORMAL,
            ShifuMessageKeys.NotEnoughScrolls.RAIN,
            isRaining
        )
}
