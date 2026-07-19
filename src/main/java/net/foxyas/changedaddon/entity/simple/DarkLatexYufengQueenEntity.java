package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.ability.api.GrabEntityAbilityExtensor;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonAbilities;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.ability.SimpleAbilityInstance;
import net.ltxprogrammer.changed.entity.AttributePresets;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.ai.LatexAssimilationDecision.Method;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexEntity;
import net.ltxprogrammer.changed.entity.beast.DarkLatexEntity;
import net.ltxprogrammer.changed.entity.latex.LatexType;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedLatexTypes;
import net.ltxprogrammer.changed.init.ChangedTransfurVariants;
import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.Objects;

public class DarkLatexYufengQueenEntity extends AbstractDarkLatexEntity implements IVariantExtraStats, GrabEntityAbilityExtensor.IOverrideGrabAbilityTargetConditions, IAlphaAbleEntity {

    protected final SimpleAbilityInstance summonPups;

    public DarkLatexYufengQueenEntity(EntityType<? extends DarkLatexYufengQueenEntity> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        summonPups = registerAbility(ability -> this.wantToSummon(), new SimpleAbilityInstance(ChangedAddonAbilities.SUMMON_DL_PUP.get(), IAbstractChangedEntity.forEntity(this)));
    }

    public static AttributeSupplier.Builder createLatexAttributes() {
        return ChangedEntity.createLatexAttributes().add(ForgeMod.ENTITY_REACH.get()).add(ForgeMod.BLOCK_REACH.get());
    }

    @Override
    protected TransfurVariant<?> getTransfurVariant(Method method) {
        if (method == Method.ABSORPTION) {
            return super.getSelfVariant();
        }

        return ChangedTransfurVariants.DARK_LATEX_YUFENG.get();
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        AttributePresets.dragonLike(attributes);
        Objects.requireNonNull(attributes.getInstance(Attributes.MOVEMENT_SPEED)).setBaseValue(1.15f);
        Objects.requireNonNull(attributes.getInstance(ForgeMod.ENTITY_REACH.get())).setBaseValue(3.5F); // oh my LARD!!! it was a pain in the ass to figure out how to modify that attribute
        Objects.requireNonNull(attributes.getInstance(ForgeMod.BLOCK_REACH.get())).setBaseValue(5F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_DAMAGE)).setBaseValue(4F);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue(5f);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get())).setBaseValue(1.5F);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get())).setBaseValue(2F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ATTACK_KNOCKBACK)).setBaseValue(1.5F);
        Objects.requireNonNull(attributes.getInstance(Attributes.MAX_HEALTH)).setBaseValue(40F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR)).setBaseValue(8F);
        Objects.requireNonNull(attributes.getInstance(Attributes.ARMOR_TOUGHNESS)).setBaseValue(2F);
        Objects.requireNonNull(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE)).setBaseValue(0.6F);
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        if (LatexType.getEntityLatexType(livingEntity) == ChangedLatexTypes.WHITE_LATEX.get()) {
            return false;
        } else {
            return super.targetSelectorTest(livingEntity);
        }
    }

    public boolean wantToSummon() {
        return getTarget() != null;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.fromInt(0x3d3d3d);
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.ABSORPTION;
    }

    @Override
    public float getFlyingSpeed() {
        return super.getFlyingSpeed() * 1.5f;
    }

//    @Override
//    public void variantTick(Level level) {
//        super.variantTick(level);
//        GrabEntityAbilityInstance grab = getAbilityInstance(ChangedAbilities.GRAB_ENTITY_ABILITY.get());
//
//        if (grab != null) {
//            if (grab instanceof GrabEntityAbilityExtensor extensor) extensor.setAllowGrabTransfurred(true);
//        }
//    }

    @Override
    public boolean canGrabEntity(LivingEntity livingTarget, GrabEntityAbilityInstance grabEntityAbilityInstance) {
        boolean couldGrabEntity = GrabEntityAbilityExtensor.IOverrideGrabAbilityTargetConditions.super.canGrabEntity(livingTarget, grabEntityAbilityInstance);
        if (!couldGrabEntity && EntityUtil.maybeGetOverlaying(livingTarget) instanceof ChangedEntity changedEntity && changedEntity instanceof DarkLatexEntity) {
            if (changedEntity instanceof WolfyEntity) {
                return false;
            }

            var selfAlpha = this;
            if (changedEntity instanceof IAlphaAbleEntity targetAlpha) {
                if (targetAlpha.isAlpha() && !selfAlpha.isAlpha()) {
                    return false;
                }
                if (targetAlpha.isAlpha() && selfAlpha.isAlpha()) {
                    return selfAlpha.alphaAdditionalScale() >= targetAlpha.alphaAdditionalScale();
                }
            }

            return !(changedEntity instanceof DarkLatexYufengQueenEntity);
        }

        return couldGrabEntity;
    }

    @Override
    public boolean isAlpha() {
        return true;
    }

    @Override
    public float alphaAdditionalScale() {
        return 0; // 0 is "use the normal size"
    }

    @Override
    public void setAlpha(boolean alpha) {
        if (!entityData.hasItem(IS_ALPHA)) {
            return;
        }

        if (this.isAlpha() != alpha) {
            this.getEntityData().set(IS_ALPHA, alpha);
            this.refreshDimensions();
            refreshAttributes(this);
            refreshAttributesForHost(this);
        }
    }

    @Override
    public void setAlphaScale(float scale) {
        if (!entityData.hasItem(ALPHA_SCALE)) {
            return;
        }

        if (this.alphaAdditionalScale() != scale) {
            this.getEntityData().set(ALPHA_SCALE, scale);
            this.refreshDimensions();
            refreshAttributes(this);
            refreshAttributesForHost(this);
        }
    }
}
