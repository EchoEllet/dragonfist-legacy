package dev.echoellet.dragonfist_legacy.item.scrolls

import dev.echoellet.dragonfist_legacy.entity.shifu.trade.ScrollTradeInfo
import dev.echoellet.dragonfist_legacy.entity.shifu.trade.ScrollTradeMessageFormatter
import dev.echoellet.dragonfist_legacy.generated.LangKeys
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag

abstract class ScrollItem(
    val tier: ScrollTier,
    private val itemName: Component,
    itemProperties: Properties
) : Item(
    itemProperties
        .stacksTo(64)
        .rarity(tier.minecraftRarity)
) {
    override fun getName(stack: ItemStack): Component = itemName

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltipComponents: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)

        tooltipComponents.add(
            Component.translatable(LangKeys.TOOLTIP_SCROLL_SCROLL_INFO)
        )

        val tradeInfo = ScrollTradeInfo.byTier(tier)
        val message = ScrollTradeMessageFormatter.format(tradeInfo)

        tooltipComponents.add(Component.literal(message))

        tooltipComponents.add(Component.literal("─".repeat(20)).withStyle(ChatFormatting.DARK_GRAY)) // Divider line
        tooltipComponents.add(Component.translatable(LangKeys.TOOLTIP_SCROLL_KEEP_IT))
    }
}

// For color/glow in GUI
private val ScrollTier.minecraftRarity
    get() = when (this) {
        ScrollTier.VERY_COMMON -> Rarity.COMMON
        ScrollTier.COMMON -> Rarity.COMMON
        ScrollTier.UNCOMMON -> Rarity.UNCOMMON
        ScrollTier.RARE -> Rarity.UNCOMMON
        ScrollTier.EPIC -> Rarity.RARE
        ScrollTier.LEGENDARY -> Rarity.EPIC
    }
