package dev.echoellet.dragonfist_legacy

import net.neoforged.neoforge.common.ModConfigSpec

object Config {
    val MIN_KNIGHTS_SPAWN: ModConfigSpec.IntValue
    val MAX_KNIGHTS_SPAWN: ModConfigSpec.IntValue

    val SPEC: ModConfigSpec

    init {
        val builder: ModConfigSpec.Builder = ModConfigSpec.Builder()

        builder.push("knights")

        MIN_KNIGHTS_SPAWN = builder
            .comment("Minimum number of knights to spawn whenever an Iron Golem spawns in a village structure.")
            .defineInRange("minKnightsSpawn", 2, 0, 50)

        MAX_KNIGHTS_SPAWN = builder
            .comment("Minimum number of knights that spawn whenever an Iron Golem spawns in a village structure.")
            .defineInRange("maxKnightsSpawn", 5, 0, 50)

        builder.pop()

        SPEC = builder.build()
    }
}
