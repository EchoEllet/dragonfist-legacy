package dev.echoellet.dragonfist_legacy.entity.bandit.rank.regular

import dev.echoellet.dragonfist_legacy.entity.bandit.BanditRenderer
import dev.echoellet.dragonfist_legacy.entity.common.humanoid.HumanoidVariant
import dev.echoellet.dragonfist_legacy.generated.ModAssetPaths
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class BanditRegularRenderer(context: EntityRendererProvider.Context) : BanditRenderer<BanditRegularEntity>(context) {
    override fun getMaleTexturePath(entity: BanditRegularEntity): String = getMaleSkinPath(entity.variant)

    override fun getFemaleTexturePath(entity: BanditRegularEntity): String = getFemaleSkinPath(entity.variant)

    companion object {
        private fun getMaleSkinPath(variant: HumanoidVariant): String = when (variant) {
            HumanoidVariant.Variant1 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_MALE_1_PNG
            HumanoidVariant.Variant2 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_MALE_2_PNG
            HumanoidVariant.Variant3 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_MALE_3_PNG
            HumanoidVariant.Variant4 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_MALE_4_PNG
        }

        private fun getFemaleSkinPath(variant: HumanoidVariant): String = when (variant) {
            HumanoidVariant.Variant1 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_FEMALE_1_PNG
            HumanoidVariant.Variant2 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_FEMALE_2_PNG
            HumanoidVariant.Variant3 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_FEMALE_3_PNG
            HumanoidVariant.Variant4 -> ModAssetPaths.TEXTURES_ENTITIES_BANDIT_REGULAR_FEMALE_4_PNG
        }
    }
}
