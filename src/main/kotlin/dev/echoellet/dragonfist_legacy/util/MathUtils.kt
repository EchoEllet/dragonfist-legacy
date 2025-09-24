package dev.echoellet.dragonfist_legacy.util

import net.minecraft.util.Mth

object MathUtils {
    // Copied from: https://github.com/Epic-Fight/epicfight/blob/70299d5f0cb5f1bac8dabd0359065684b1ebe432/src/main/java/yesman/epicfight/api/utils/math/MathUtils.java#L92-L114
    // to keep this project independent of Epic Fight internals.
    fun rotLerp(from: Float, to: Float, limit: Float): Float {
        var f = Mth.wrapDegrees(to - from)

        if (f > limit) {
            f = limit
        }

        if (f < -limit) {
            f = -limit
        }

        var f1 = from + f

        while (f1 >= 180.0f) {
            f1 -= 360.0f
        }

        while (f1 <= -180.0f) {
            f1 += 360.0f
        }

        return f1
    }
}
