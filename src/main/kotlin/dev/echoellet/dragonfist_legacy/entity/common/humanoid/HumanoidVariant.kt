package dev.echoellet.dragonfist_legacy.entity.common.humanoid

/**
 * Represents the variant of a humanoid entity.
 *
 * This can be used for humanoid entities that require a variant.
 *
 * @property id A numeric identifier persisted in the Minecraft entity's NBT data.
 *              This value **must not be changed**, as it would break saved data for existing entities.
 */
enum class HumanoidVariant(val id: Int) {
    Variant1(0),
    Variant2(1),
    Variant3(2),
    Variant4(3);

    companion object {
        val DEFAULT = Variant1

        fun fromId(id: Int): HumanoidVariant = HumanoidVariant.entries.firstOrNull { it.id == id } ?: DEFAULT
    }
}
