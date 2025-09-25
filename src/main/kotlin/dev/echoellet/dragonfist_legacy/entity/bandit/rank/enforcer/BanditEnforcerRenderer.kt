package dev.echoellet.dragonfist_legacy.entity.bandit.rank.enforcer

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class BanditEnforcerRenderer(context: EntityRendererProvider.Context) : BanditRenderer<BanditEnforcerEntity>(context) {
    override fun getMaleTexturePath(entity: BanditEnforcerEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_ENFORCER_MALE_PNG

    override fun getFemaleTexturePath(entity: BanditEnforcerEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_ENFORCER_FEMALE_PNG
}
