package dev.echoellet.dragonfist_legacy.mixin;

import dev.echoellet.dragonfist_legacy.entity.knight.VillageKnightSpawnEventHandler;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(
            method = "finalizeSpawn",
            at = @At("TAIL")
    )
    private void afterFinalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType spawnType,
            SpawnGroupData spawnGroupData,
            CallbackInfoReturnable<SpawnGroupData> cir
    ) {
        final Mob entity = ((Mob) (Object) this);
        VillageKnightSpawnEventHandler.INSTANCE.onFinalizeSpawn(entity, spawnType, level);
    }
}
