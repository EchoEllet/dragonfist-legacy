package dev.echoellet.dragonfist_legacy.entity.bandit

import dev.echoellet.dragonfist_legacy.DragonFistLegacy
import dev.echoellet.dragonfist_legacy.entity.common.gender.Gender
import dev.echoellet.dragonfist_legacy.entity.common.humanoid.PlayerLikeRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.resources.ResourceLocation

// TODO: Stronger client-only separation (for all Renders or subclasses of PlayerLikeRenderer)
abstract class BanditRenderer<T : BanditEntity>(context: EntityRendererProvider.Context) :
    PlayerLikeRenderer<T>(context) {

    abstract fun getMaleTexturePath(entity: T): String
    abstract fun getFemaleTexturePath(entity: T): String

    private fun getSkinPath(entity: T): String = when (entity.gender) {
        Gender.Male -> getMaleTexturePath(entity)
        Gender.Female -> getFemaleTexturePath(entity)
    }

    override fun getTextureLocation(entity: T): ResourceLocation = DragonFistLegacy.rl(getSkinPath(entity))
}
