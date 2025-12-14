package dev.echoellet.dragonfist_legacy.entity.common.gender

/**
 * Represents the gender of an entity.
 *
 * This can be used for living entities that require a gender.
 *
 * @property id A numeric identifier persisted in the Minecraft entity's NBT data.
 *              This value **must not be changed**, as it would break saved data for existing entities.
 */
enum class Gender(
    val id: Int
) {
    Male(0), Female(1);

    companion object {
        val DEFAULT = Male

        fun fromId(id: Int): Gender = entries.firstOrNull { it.id == id } ?: DEFAULT
    }

    val isMale get() = this == Male
}
