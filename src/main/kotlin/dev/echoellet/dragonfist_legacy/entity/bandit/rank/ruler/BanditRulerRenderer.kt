package dev.echoellet.dragonfist_legacy.entity.bandit.rank.ruler

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider

class BanditRulerRenderer(context: EntityRendererProvider.Context) : BanditRenderer<BanditRulerEntity>(context) {
    override fun getMaleTexturePath(entity: BanditRulerEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_RULER_MALE_PNG

    override fun getFemaleTexturePath(entity: BanditRulerEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_RULER_FEMALE_PNG
}
