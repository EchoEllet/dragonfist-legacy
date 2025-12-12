package dev.echoellet.dragonfist_legacy.entity.bandit.rank

/**
 * Represents the rank of a bandit and its general spawning behavior.
 *
 * @property level The hierarchical level of the bandit rank. Higher numbers indicate
 *                 more important or powerful bandits.
 * @property isCommon Indicates whether this bandit rank is common in the world.
 *                    - `true`: The bandit can spawn freely around the world.
 *                    - `false`: The bandit is rarer and typically found only in structures.
 */
// Note for project maintainers: This enum must stay in sync with the `BanditRank` enum defined in build.gradle.kts.
enum class BanditRank(val level: Int, val isCommon: Boolean) {
    Regular(1, true), // Bandit
    Enforcer(2, true), // Bandit Enforcer
    Champion(3, false), // Bandit Champion
    Elite(4, false), // Bandit Elite
    Leader(5, false), // Bandit Leader

    /**
     * King for male, Queen for female
     */
    Ruler(6, false) // King/Queen of the Bandits
}
