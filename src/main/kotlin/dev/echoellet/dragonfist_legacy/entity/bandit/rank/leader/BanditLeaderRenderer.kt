package dev.echoellet.dragonfist_legacy.entity.bandit.rank.leader

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditRenderer
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider

class BanditLeaderRenderer(context: EntityRendererProvider.Context) : BanditRenderer<BanditLeaderEntity>(context) {
    override fun getMaleTexturePath(entity: BanditLeaderEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_LEADER_MALE_PNG

    override fun getFemaleTexturePath(entity: BanditLeaderEntity): String =
        ModAssetPaths.TEXTURES_ENTITIES_BANDIT_LEADER_FEMALE_PNG
}
