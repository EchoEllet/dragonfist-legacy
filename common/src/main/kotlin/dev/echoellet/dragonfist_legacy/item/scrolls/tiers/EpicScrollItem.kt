package dev.echoellet.dragonfist_legacy.item.scrolls.tiers

import dev.echoellet.dragonfist_legacy.generated.LangKeys
import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollItem
import dev.echoellet.dragonfist_legacy.item.scrolls.ScrollTier
import net.minecraft.network.chat.Component

class EpicScrollItem : ScrollItem(
    ScrollTier.EPIC,
    Component.translatable(LangKeys.ITEM_EPIC_SCROLL),
    Properties()
        .fireResistant()
)
