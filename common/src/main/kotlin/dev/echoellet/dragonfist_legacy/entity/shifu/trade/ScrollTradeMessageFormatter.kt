package dev.echoellet.dragonfist_legacy.entity.shifu.trade

object ScrollTradeMessageFormatter {
    fun format(info: ScrollTradeInfo): String {
        val scrollLabel = if (info.scrollsRequired == 1) "scroll" else "scrolls"
        val pointLabel = if (info.abilityPoints == 1) "point" else "points"
        return "${info.scrollsRequired} $scrollLabel → ${info.abilityPoints} $pointLabel"
    }
}
