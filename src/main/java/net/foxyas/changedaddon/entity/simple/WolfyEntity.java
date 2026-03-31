package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.entity.ai.goals.abilities.MayCauseGrabDamageGoal;
import net.foxyas.changedaddon.entity.ai.goals.abilities.MayDropGrabbedEntityGoal;
import net.foxyas.changedaddon.entity.ai.goals.abilities.MayGrabTargetGoal;
import net.foxyas.changedaddon.entity.api.IGrabberEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonItems;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.mixins.abilities.AbilityControllerAccessor;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.variant.VariantExtraStats;
import net.ltxprogrammer.changed.ability.AbstractAbility;
import net.ltxprogrammer.changed.ability.AbstractAbilityInstance;
import net.ltxprogrammer.changed.ability.GrabEntityAbilityInstance;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.beast.AbstractDarkLatexWolf;
import net.ltxprogrammer.changed.entity.variant.TransfurVariant;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static net.foxyas.changedaddon.procedure.CreatureDietsHandleProcedure.DietType;

public class WolfyEntity extends AbstractDarkLatexWolf implements VariantExtraStats, IGrabberEntity {

    public static final DietType WOLFY_DIET = DietType.create("WOLFY", ChangedAddonTags.TransfurTypes.WOLF_DIET, ChangedAddonTags.Items.WOLF_DIET, List.of(ChangedAddonItems.FOXTA.get(), ChangedItems.ORANGE.get()));

    public WolfyEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.WOLFY.get(), world);
    }

    public WolfyEntity(EntityType<WolfyEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        this.setAttributes(getAttributes());
        setNoAi(false);
        setPersistenceRequired();

        if (this.grabEntityAbilityInstance == null) {
            this.grabEntityAbilityInstance = this.createGrabAbility();
        }
        this.setCanUseGrab(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 14);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 2);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    @SuppressWarnings("DataFlowIssue")
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((1));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((14));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(25.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.20f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.5f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(2);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    @Override
    public boolean tryAbsorbTarget(LivingEntity target, IAbstractChangedEntity source, float amount, @Nullable List<TransfurVariant<?>> possibleMobFusions) {
        boolean thisOrUnderlyingPlayerHasEffect = (
                (this.getUnderlyingPlayer() != null && this.getUnderlyingPlayer().hasEffect(MobEffects.DAMAGE_BOOST)) || this.hasEffect(MobEffects.DAMAGE_BOOST)
        );

        if (thisOrUnderlyingPlayerHasEffect || target.hasEffect(ChangedAddonMobEffects.LATEX_EXPOSURE.get())) {
            return super.tryAbsorbTarget(target, source, amount, possibleMobFusions);
        }
        return false;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean variantOverrideSwim() {
        if (this.maybeGetUnderlying() instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            return transfurVariant != null && player.isEyeInFluid(FluidTags.LAVA);
        }

        return false;
    }

    @Override
    public boolean variantOverrideSwimUpdate() {
        if (this.maybeGetUnderlying() instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            return transfurVariant != null && player.isEyeInFluid(FluidTags.LAVA);
        }

        return false;
    }

    @Override
    public boolean variantOverrideIsInWater() {
        if (this.maybeGetUnderlying() instanceof Player player) {
            TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(player);
            return transfurVariant != null && player.level().getFluidState(player.blockPosition()).is(FluidTags.LAVA);
        }

        return false;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.NONE;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return super.isAlliedTo(entity);
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        return HairStyle.BALD.get();
    }

    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.getAll();
    }

    public Color3 getHairColor(int layer) {
        return Color3.DARK;
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public boolean isMaskless() {
        return true;
    }

    @Override
    public boolean isAbleToGrab() {
        return true;
    }

    @Override
    public @Nullable GrabEntityAbilityInstance getGrabAbilityInstance() {
        return super.getGrabAbility();
    }

    @Override
    public LivingEntity getGrabbedEntity() {
        return grabEntityAbilityInstance != null ? grabEntityAbilityInstance.grabbedEntity : null;
    }

    @Override
    public PathfinderMob asMob() {
        return this;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //this.goalSelector.addGoal(1, new GrabTargetGoal(this, 0.4f, false));
        this.goalSelector.addGoal(10, new MayDropGrabbedEntityGoal(this));
        this.goalSelector.addGoal(10, new MayGrabTargetGoal(this));
        this.goalSelector.addGoal(10, new MayCauseGrabDamageGoal(this));
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.mayTickGrabAbility();
    }

    @Override
    protected void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        mayDropGrabbedEntity(pDamageSource, pDamageAmount);
        super.actuallyHurt(pDamageSource, pDamageAmount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.saveGrabAbilityInTag(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readGrabAbilityInTag(tag);
    }

    @Override
    public @Nullable GrabEntityAbilityInstance getGrabAbility() {
        return this.grabEntityAbilityInstance;
    }

    @Override
    public <A extends AbstractAbilityInstance> A getAbilityInstance(AbstractAbility<A> ability) {
        return (A) (this.grabEntityAbilityInstance != null && ability == this.grabEntityAbilityInstance.ability ? this.grabEntityAbilityInstance : super.getAbilityInstance(ability));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        if (!this.entityData.hasItem(GRAB_COOLDOWN)) {
            entityData.define(GRAB_COOLDOWN, 0);
        }
    }

    @Override
    public int getGrabCooldown() {
        return this.entityData.get(GRAB_COOLDOWN);
    }

    @Override
    public void setGrabCooldown(int grabCooldown) {
        GrabEntityAbilityInstance grabAbilityInstance = this.getGrabAbilityInstance();
        if (grabAbilityInstance != null) {
            AbstractAbility.Controller controller = grabAbilityInstance.getController();
            controller.forceCooldown(grabCooldown);
            if (controller instanceof AbilityControllerAccessor abilityControllerAccessor) {
                this.entityData.set(GRAB_COOLDOWN, abilityControllerAccessor.getCooldownTicksRemaining());
            }
        }
    }

    @Override
    public boolean canUseGrab() {
        return this.entityData.get(CAN_USE_GRAB);
    }

    @Override
    public void setCanUseGrab(boolean value) {
        this.entityData.set(CAN_USE_GRAB, value);
    }

    public Color3 getDripColor() {
        Color3 color = Color3.getColor("#000000");
        if (random.nextInt(10) > 5) {
            color = Color3.getColor("#393939");
        } else {
            color = Color3.getColor("#303030");
        }
        return color;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#303030");
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset();
    }

    @Override
    public List<DietType> getExtraDietTypes() {
        return List.of(WOLFY_DIET);
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.is(DamageTypes.LIGHTNING_BOLT))
            return false;
        if (source.is(DamageTypeTags.IS_EXPLOSION))
            return false;
        if (source.is(DamageTypeTags.IS_FIRE))
            return false;
        return super.hurt(source, amount);
    }

    @Mod.EventBusSubscriber
    public static class WolfyAttackedEvent {

        @SubscribeEvent
        public static void onEntityAttacked(LivingAttackEvent event) {
            Entity entity = event.getEntity();
            if (!(entity instanceof Player player)) return;

            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance == null || !instance.is(ChangedAddonTransfurVariants.WOLFY)) return;

            DamageSource damagesource = event.getSource();
            if (damagesource.is(DamageTypes.THORNS)) {
                event.setCanceled(true);
                return;
            }

            if (damagesource.is(DamageTypeTags.IS_FIRE)) {
                event.setCanceled(true);
                return;
            }

            if (damagesource.is(DamageTypeTags.IS_EXPLOSION)) {
                event.setCanceled(true);
                return;
            }

            if (damagesource.is(DamageTypes.LIGHTNING_BOLT)) {
                event.setCanceled(true);
            }
        }
    }

}
