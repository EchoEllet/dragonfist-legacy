package dev.echoellet.dragonfist_legacy.mixin;

import dev.echoellet.dragonfist_legacy.entity.shifu.combat.ShifuPlayerTrainingPolicy;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @ModifyVariable(
            method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            at = @At("HEAD"),
            argsOnly = true
    )
    private float clampDamage(final float amount, final DamageSource source) {
        final LivingEntity target = ((LivingEntity) (Object) this);
        final Entity attacker = source.getEntity();

        Optional<Float> maybeClamp = ShifuPlayerTrainingPolicy.Companion.clampDamageIfNeeded(target, attacker, amount);
        if (maybeClamp.isPresent()) {
            return maybeClamp.get();
        }

        return amount;
    }
}
