package dev.echoellet.dragonfist_legacy.entity.common.humanoid

import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HumanoidMobRenderer
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer
import net.minecraft.world.entity.Mob

abstract class PlayerLikeRenderer<T : Mob>(context: EntityRendererProvider.Context) :
    HumanoidMobRenderer<T, HumanoidModel<T>>(
        context,
        HumanoidModel(context.bakeLayer(ModelLayers.PLAYER)),
        SHADOW_RADIUS,
    ) {

    companion object {
        private const val SHADOW_RADIUS = 0.5f
    }

    init {
        this.addLayer(
            HumanoidArmorLayer(
                this,
                HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
                HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
                context.modelManager,
            )
        )
    }
}
