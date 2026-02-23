package net.foxyas.changedaddon.ability;

import net.foxyas.changedaddon.entity.advanced.AbstractDazedEntity;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class DazedPuddleAbility extends SimpleAbility {

    @Override
    public Component getAbilityName(IAbstractChangedEntity entity) {
        return Component.translatable("ability.changed.puddle");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return ResourceLocation.parse("changed:textures/abilities/puddle.png");
    }

    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AbstractDazedEntity dazedLatexEntity) {
            entity.getEntity().playSound(ChangedSounds.TRANSFUR_BY_LATEX.get(), 1, 1);
            dazedLatexEntity.setMorphed(true);
        }
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        entity.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 2, false, false, false));
        entity.getEntity().addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 5, 1, false, false, false));
        var TransfurDmgAttribute = entity.getEntity().getAttribute(ChangedAttributes.TRANSFUR_DAMAGE.get());
        if (TransfurDmgAttribute == null) {
            return;
        }
        float TransfurDmgAmount = (float) TransfurDmgAttribute.getValue();
        entity.getLevel().getEntitiesOfClass(LivingEntity.class, entity.getChangedEntity().getBoundingBox().inflate(0.25, 0, 0.25)).forEach(caught -> {
            if (caught == entity.getEntity())
                return;
            if (caught.getType().is(ChangedTags.EntityTypes.HUMANOIDS)) {
                if (caught instanceof Player player && ProcessTransfur.getPlayerTransfurVariant(player) == null) {
                    ProcessTransfur.progressTransfur(player, TransfurDmgAmount, entity.getTransfurVariant(), TransfurContext.hazard(TransfurCause.FLOOR_HAZARD));
                    caught.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2, false, false, false));
                }
            }
        });
    }

    @Override
    public void stopUsing(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof AbstractDazedEntity dazedLatexEntity) {
            dazedLatexEntity.setMorphed(false);
        }
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        TransfurVariant<?> var = entity.getTransfurVariant();
        if (var == null) return false;

        return var.is(ChangedAddonTransfurVariants.DAZED_LATEX) || var.is(ChangedAddonTransfurVariants.BUFF_DAZED_LATEX);
    }
}