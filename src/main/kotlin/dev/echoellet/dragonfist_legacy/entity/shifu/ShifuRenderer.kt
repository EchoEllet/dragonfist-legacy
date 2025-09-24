package dev.echoellet.dragonfist_legacy.entity.shifu

import dev.echoellet.dragonfist_legacy.entity.common.humanoid.PlayerLikeRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import dev.echoellet.dragonfist_legacy.util.ModResources
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class ShifuRenderer(context: EntityRendererProvider.Context) :
    PlayerLikeRenderer<ShifuEntity>(context) {
    override fun getTextureLocation(entity: ShifuEntity): ResourceLocation =
        ModResources.id(ModAssetPaths.TEXTURES_ENTITIES_SHIFU_PNG)
}
