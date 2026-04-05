package net.foxyas.changedaddon.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = GrabEntityPacket.class, remap = false)
public abstract class GrabEntityPacketMixin {

    @Shadow
    @Final
    public int sourceEntity;

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;is(Lnet/minecraft/tags/TagKey;)Z", remap = true),
            method = "lambda$handle$4")
    private boolean ignoreTagCheck(boolean original, @Local(name = "livingTarget") LivingEntity livingTarget, @Local(argsOnly = true) Level level) {
        Entity entity = level.getEntity(sourceEntity);
        if (entity instanceof LivingEntity sourceLiving) {
            Player player = EntityUtil.playerOrNull(sourceLiving);
            if (player != null && !ProcessTransfur.isPlayerTransfurred(player)) {
                return original;
            }

            IAbstractChangedEntity iAbstractChangedEntity = IAbstractChangedEntity.forEither(sourceLiving);
            if (iAbstractChangedEntity != null) {
                GrabEntityAbilityInstance abilityInstance = iAbstractChangedEntity.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
                if (abilityInstance instanceof GrabEntityAbilityExtensor abilityExtensor) {
                    return abilityExtensor.canGrabEntity(livingTarget) || original;//TODO add allowGrabTransfurred check?
                }
            }
        }
        return original;//TODO add allowGrabTransfurred check?
    }
}
