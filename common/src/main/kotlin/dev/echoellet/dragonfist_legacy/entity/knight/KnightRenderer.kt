package dev.echoellet.dragonfist_legacy.entity.knight

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.common.gender.Gender
import dev.echoellet.dragonfist_legacy.entity.common.humanoid.PlayerLikeRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

class KnightRenderer(context: EntityRendererProvider.Context) : PlayerLikeRenderer<KnightEntity>(context) {
    override fun getTextureLocation(entity: KnightEntity): ResourceLocation =
        DragonFistLegacy.rl(
            when(entity.gender) {
                Gender.Male -> ModAssetPaths.TEXTURES_ENTITIES_KNIGHT_MALE_PNG
                Gender.Female -> ModAssetPaths.TEXTURES_ENTITIES_KNIGHT_FEMALE_PNG
            }
        )
}
