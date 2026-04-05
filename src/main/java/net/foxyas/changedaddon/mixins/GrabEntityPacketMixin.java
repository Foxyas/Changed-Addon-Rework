package net.foxyas.changedaddon.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.init.ChangedAbilities;
import net.ltxprogrammer.changed.network.packet.GrabEntityPacket;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
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
    private boolean ignoreTagCheck(boolean original, @Local(name = "livingTarget") LivingEntity livingTarget, @Local(name = "sender") ServerPlayer sender) {
        if (sender.getId() != sourceEntity) return original;

        if (!ProcessTransfur.isPlayerTransfurred(sender)) return original;

        IAbstractChangedEntity iAbstractChangedEntity = IAbstractChangedEntity.forPlayer(sender);
        GrabEntityAbilityInstance abilityInstance = iAbstractChangedEntity.getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
        if (abilityInstance == null) return original;

        return ((GrabEntityAbilityExtensor)abilityInstance).canGrabEntity(livingTarget) || original;
    }
}
