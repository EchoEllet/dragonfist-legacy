package dev.echoellet.dragonfist_legacy.entity.shifu

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.common.humanoid.PlayerLikeRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider

class ShifuRenderer(context: EntityRendererProvider.Context) :
    PlayerLikeRenderer<ShifuEntity>(context) {
    override fun getTextureLocation(entity: ShifuEntity) =
        DragonFistLegacy.identifier(ModAssetPaths.TEXTURES_ENTITIES_SHIFU_PNG)
}
