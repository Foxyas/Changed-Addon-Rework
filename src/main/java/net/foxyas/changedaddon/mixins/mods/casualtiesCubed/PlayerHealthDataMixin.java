package net.foxyas.changedaddon.mixins.mods.casualtiesCubed;

import net.foxyas.changedaddon.entity.bosses.Experiment009Entity;
import net.foxyas.changedaddon.entity.bosses.Experiment10BossEntity;
import net.foxyas.changedaddon.extension.RequiredMods;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.casualties_cubed.limbs.PlayerHealthData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerHealthData.class, remap = false)
@RequiredMods("casualties_cubed")
public abstract class PlayerHealthDataMixin {

    //    @WrapOperation(method = "handleBodyTemperature", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 0))
    //    public float checkEntityHeatInsulation(float pValue, float pMin, float pMax, Operation<Float> original, @Local(argsOnly = true) ServerPlayer player, @Local(name = "INSULATION_PER_POINT") float insulationPerPoints) {

    /*@WrapOperation(method = "updateTemperature", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F", ordinal = 0, remap = true))
    public float checkEntityHeatInsulation(float pValue, float pMin, float pMax, Operation<Float> original, @Local(argsOnly = true) Player player, @Local(name = "INSULATION_PER_POINT") float insulationPerPoints) {
        if (EntityUtil.maybeGetOverlaying(player) instanceof ChangedEntity changedEntity) {
            if (changedEntity instanceof IVariantExtraStats variantExtraStats) {
                float variantHeatInsulation = variantExtraStats.getHeatInsulationScale() * insulationPerPoints;
                if (variantExtraStats.isHeatInsulationClamped()) {
                    return original.call(pValue + variantHeatInsulation, pMin, pMax);
                } else {
                    return original.call(pValue, pMin, pMax) * variantHeatInsulation;
                }
            }
        }

        return original.call(pValue, pMin, pMax);
    }*/

    @Shadow
    private float stamina;

    @Inject(
            method = "handleBody",
            at = @At(
                    value = "TAIL"
            )
    )
    private void makeImmuneToExhaustionWhenFightingBosses(ServerPlayer player, CallbackInfo ci) {
        LivingEntity lastHurtByMob = player.getLastHurtByMob();
        if (lastHurtByMob instanceof Experiment009Entity || lastHurtByMob instanceof Experiment10BossEntity) {
            if (lastHurtByMob.isDeadOrDying() && !lastHurtByMob.isRemoved()) {
                if (this.stamina < 60) {
                    this.stamina = 25f;
                }
            } else {
                if (this.stamina < 50) {
                    this.stamina = 50;
                }
            }
        }
    }
}
