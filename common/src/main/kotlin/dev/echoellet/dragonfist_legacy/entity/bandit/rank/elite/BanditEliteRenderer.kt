package dev.echoellet.dragonfist_legacy.entity.bandit.rank.elite

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider

class BanditEliteRenderer(context: EntityRendererProvider.Context) : BanditRenderer<BanditEliteEntity>(context) {
    override fun getMaleTexturePath(entity: BanditEliteEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_ELITE_MALE_PNG

    override fun getFemaleTexturePath(entity: BanditEliteEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_ELITE_FEMALE_PNG
}
