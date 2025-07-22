package net.foxyas.changedaddon.abilities;

import net.foxyas.changedaddon.entity.DazedEntity;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbility;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurContext;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;

import java.util.UUID;


public class DazedPuddleAbility extends SimpleAbility {

    public final AttributeModifier PuddleReachAttributeMod = new AttributeModifier(UUID.fromString("393f142c-fcec-4a4f-a233-6c86b64f6468"),"ReachPuddleMod", -0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public TranslatableComponent getAbilityName(IAbstractChangedEntity entity) {
        return new TranslatableComponent("ability.changed.puddle");
    }

    public ResourceLocation getTexture(IAbstractChangedEntity entity) {
        return new ResourceLocation("changed:textures/abilities/puddle.png");
    }
    @Override
    public void startUsing(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof DazedEntity dazedEntity) {
            entity.getEntity().playSound(ChangedSounds.POISON, 1, 1);
            dazedEntity.setMorphed(true);
        }
    }

    @Override
    public void tick(IAbstractChangedEntity entity) {
        entity.getEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 2, false, false, false));
        entity.getEntity().addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN,5,1,false,false,false));
        if (entity.getEntity() instanceof Player player){
            if (player.getAttribute(ForgeMod.REACH_DISTANCE.get()) != null
                    && !player.getAttribute(ForgeMod.REACH_DISTANCE.get()).hasModifier(PuddleReachAttributeMod)){
                player.getAttribute(ForgeMod.REACH_DISTANCE.get()).addTransientModifier(PuddleReachAttributeMod);
            } 
            if (player.getAttribute(ForgeMod.ATTACK_RANGE.get()) != null
                    && !player.getAttribute(ForgeMod.ATTACK_RANGE.get()).hasModifier(PuddleReachAttributeMod)){
                player.getAttribute(ForgeMod.ATTACK_RANGE.get()).addTransientModifier(PuddleReachAttributeMod);
            }
        }
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
                    ProcessTransfur.progressTransfur(player, TransfurDmgAmount, ChangedAddonTransfurVariants.DAZED_LATEX.get(), TransfurContext.hazard(TransfurCause.FLOOR_HAZARD));
                    caught.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2, false, false, false));
                }
            }
        });
    }

    @Override
    public void stopUsing(IAbstractChangedEntity entity) {
        if (entity.getChangedEntity() instanceof DazedEntity dazedEntity) {
            dazedEntity.setMorphed(false);
        }
        if (entity.getEntity() instanceof Player player){
            if (player.getAttribute(ForgeMod.REACH_DISTANCE.get()) != null
                    && player.getAttribute(ForgeMod.REACH_DISTANCE.get()).hasModifier(PuddleReachAttributeMod)){
                player.getAttribute(ForgeMod.REACH_DISTANCE.get()).removeModifier(PuddleReachAttributeMod);
            } 
            if (player.getAttribute(ForgeMod.ATTACK_RANGE.get()) != null
                    && player.getAttribute(ForgeMod.ATTACK_RANGE.get()).hasModifier(PuddleReachAttributeMod)){
                player.getAttribute(ForgeMod.ATTACK_RANGE.get()).removeModifier(PuddleReachAttributeMod);
            }
        }
    }

    @Override
    public UseType getUseType(IAbstractChangedEntity entity) {
        return UseType.HOLD;
    }

    @Override
    public boolean canUse(IAbstractChangedEntity entity) {
        return true;
    }
}