package dev.echoellet.dragonfist_legacy.entity.bandit.rank.champion

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider

class BanditChampionRenderer(context: EntityRendererProvider.Context) : BanditRenderer<BanditChampionEntity>(context) {
    override fun getMaleTexturePath(entity: BanditChampionEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_CHAMPION_MALE_PNG

    override fun getFemaleTexturePath(entity: BanditChampionEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_CHAMPION_FEMALE_PNG
}
