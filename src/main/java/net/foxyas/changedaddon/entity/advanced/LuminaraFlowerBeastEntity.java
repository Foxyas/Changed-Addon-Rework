package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.entity.api.IBestiaryEntityData;
import net.foxyas.changedaddon.entity.api.ICustomPatReaction;
import net.foxyas.changedaddon.entity.api.IDynamicRideOffsetEntity;
import net.foxyas.changedaddon.entity.defaults.AbstractBasicOrganicChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.init.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.process.variantsExtraStats.diets.FoodDietEntry;
import net.foxyas.changedaddon.util.ColorUtil;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.FoxyasUtil;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.foxyas.changedaddon.variant.IVariantExtraStats;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.PowderSnowWalkable;
import net.ltxprogrammer.changed.entity.TransfurCause;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedItems;
import net.ltxprogrammer.changed.init.ChangedTags;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LuminaraFlowerBeastEntity extends AbstractBasicOrganicChangedEntity implements IVariantExtraStats, ICustomPatReaction, PowderSnowWalkable, IDynamicRideOffsetEntity, IBestiaryEntityData {

    public static final List<FoodDietEntry> LUMINARA_DIET = List.of(
            new FoodDietEntry(
                    Ingredient.of(ChangedAddonTags.Items.DRAGON_DIET),
                    ConstantFloat.of(2.0f),
                    ConstantFloat.of(0.5f),
                    List.of(),
                    false
            ),
            new FoodDietEntry(
                    Ingredient.of(Items.CHORUS_FRUIT, ChangedItems.ORANGE.get()),
                    ConstantFloat.of(1.0f),
                    ConstantFloat.of(0.2f),
                    List.of(),
                    false
            )
    );

    private static final EntityDataAccessor<Boolean> AWAKENED = SynchedEntityData.defineId(LuminaraFlowerBeastEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HYPER_AWAKENED = SynchedEntityData.defineId(LuminaraFlowerBeastEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> COSMIC_WINGS = SynchedEntityData.defineId(LuminaraFlowerBeastEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean spawnParticles = true;

    public LuminaraFlowerBeastEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.LUMINARA_FLOWER_BEAST.get(), world);
    }

    public LuminaraFlowerBeastEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    private static void triggerVoidTransformation(Player player, LuminaraFlowerBeastEntity luminaraFlowerBeast) {
        if (luminaraFlowerBeast.isHyperAwakened()) return;

        boolean isAwakened = luminaraFlowerBeast.isAwakened();
        if (!isAwakened) luminaraFlowerBeast.setAwakened(true);

        if (tryExtractDragonBreath(player)) {
            luminaraFlowerBeast.setHyperAwakened(true);
            player.level().playSound(null, player, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 5, 1);
        } else if (isAwakened) return;

        Vec3 vec = new Vec3(0, 8, 0);
        if (luminaraFlowerBeast.isHyperAwakened()) vec = vec.scale(1.5);
        player.setDeltaMovement(vec);
        player.hurtMarked = true;

        DelayedTask.schedule(20, () -> {
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
        });

        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20 * 10, 1));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 2));

        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        float radius = 1f;
        float angle = 22.5f;
        for (float theta = 0; theta < 360; theta += angle) {
            float angleTheta = theta * Mth.DEG_TO_RAD;
            for (float phi = 0; phi <= 180; phi += angle) {
                float anglePhi = phi * Mth.DEG_TO_RAD;
                double x = player.getX() + Mth.sin(anglePhi) * Mth.cos(angleTheta) * radius;
                double y = player.getY() + Mth.cos(anglePhi) * radius;
                double z = player.getZ() + Mth.sin(anglePhi) * Mth.sin(angleTheta) * radius;
                Vec3 pos = new Vec3(x, y, z);
                ParticlesUtil.sendParticlesWithMotion(
                        player.level(),
                        ParticleTypes.REVERSE_PORTAL,
                        pos,
                        Vec3.ZERO,
                        pos.subtract(player.position()),
                        1, 1
                );
            }
        }

        serverLevel.playSound(null, player.blockPosition(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 2.0F, 0.8F);
    }

    private static void triggerOutOfBoundsTransformation(Player player, LuminaraFlowerBeastEntity luminaraFlowerBeast) {
        if (!(luminaraFlowerBeast.level().isNight())) return;
        if (luminaraFlowerBeast.isHyperAwakened() || luminaraFlowerBeast.isAwakened()) return;

        LivingEntity living = luminaraFlowerBeast.maybeGetUnderlying();
        ItemStack stack = living.getItemBySlot(EquipmentSlot.CHEST);
        if (!(stack.getItem() instanceof ElytraItem elytraItem) || !ElytraItem.isFlyEnabled(stack)) return;

        luminaraFlowerBeast.setAwakened(true);
        luminaraFlowerBeast.setHyperAwakened(true);
        luminaraFlowerBeast.setCosmicWings(true);
        player.level().playSound(null, player, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 5, 1);

        Vec3 lookVec = player.getLookAngle();
        Vec3 boostVec = lookVec.scale(1.2).add(0, 0.4, 0);
        if (luminaraFlowerBeast.isHyperAwakened()) {
            boostVec = boostVec.scale(1.5);
        }

        player.setDeltaMovement(player.getDeltaMovement().add(boostVec));
        player.hurtMarked = true;

        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20 * 10, 1));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 2));

        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        float radius = 1f;
        float angle = 22.5f;
        for (float theta = 0; theta < 360; theta += angle) {
            float angleTheta = theta * Mth.DEG_TO_RAD;
            for (float phi = 0; phi <= 180; phi += angle) {
                float anglePhi = phi * Mth.DEG_TO_RAD;
                double x = player.getX() + Mth.sin(anglePhi) * Mth.cos(angleTheta) * radius;
                double y = player.getY() + Mth.cos(anglePhi) * radius;
                double z = player.getZ() + Mth.sin(anglePhi) * Mth.sin(angleTheta) * radius;
                Vec3 pos = new Vec3(x, y, z);
                ParticlesUtil.sendParticlesWithMotion(
                        player.level(),
                        ParticleTypes.REVERSE_PORTAL,
                        pos,
                        Vec3.ZERO,
                        pos.subtract(player.position()),
                        1, 1
                );
            }
        }

        serverLevel.playSound(null, player.blockPosition(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 2.0F, 0.8F);
    }

    private static boolean tryExtractDragonBreath(Player player) {
        Inventory inv = player.getInventory();
        ItemStack stack;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            stack = inv.getItem(i);
            if (!stack.is(Items.DRAGON_BREATH)) continue;

            if (!player.isCreative()) {
                stack.shrink(1);
                if (stack.isEmpty()) inv.setItem(i, ItemStack.EMPTY);
            }
            return true;
        }
        return false;
    }

    public Vec3 getMouthPositionRoot() {
        return this.getEyePosition().subtract(0, 0.25, 0);
    }

    public Vec3 getMouthPositionRoot(float partialTicks) {
        return this.getEyePosition(partialTicks).subtract(0, 0.25, 0);
    }

    public Vec3 getMouthPosition() {
        Vec3 view = getLookAngle();
        return getMouthPositionRoot().add(view.scale(0.25f));
    }

    public Vec3 getMouthPosition(float partialTicks) {
        Vec3 view = getViewVector(partialTicks);
        return getMouthPositionRoot().add(view.scale(0.25f));
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        if (!this.isAwakened()) {
            safeSetBaseValue(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()), 1.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.MAX_HEALTH), 22.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.FOLLOW_RANGE), 40.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.MOVEMENT_SPEED), 1.0f);
            safeSetBaseValue(attributes.getInstance(ForgeMod.SWIM_SPEED.get()), 1.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_DAMAGE), 2.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.ARMOR), 0.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 0.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE), 0.0f);
            safeSetBaseValue(attributes.getInstance(ForgeMod.BLOCK_REACH.get()), 4.5F);
            safeSetBaseValue(attributes.getInstance(ForgeMod.ENTITY_REACH.get()), 3.0F);
            safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_KNOCKBACK), 0.0f);
        } else if (this.isHyperAwakened()) {
            safeSetBaseValue(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()), 7.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.MAX_HEALTH), 40.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.FOLLOW_RANGE), 126.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.MOVEMENT_SPEED), 1.25f);
            safeSetBaseValue(attributes.getInstance(ForgeMod.SWIM_SPEED.get()), 1.25f);
            safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_DAMAGE), 6.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.ARMOR), 10.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 6.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE), 0.0f);
            safeSetBaseValue(attributes.getInstance(ForgeMod.BLOCK_REACH.get()), 6.0F);
            safeSetBaseValue(attributes.getInstance(ForgeMod.ENTITY_REACH.get()), 4.0F);
            safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_KNOCKBACK), 2.0f);
        } else {
            safeSetBaseValue(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()), 5.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.MAX_HEALTH), 40.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.FOLLOW_RANGE), 126.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.MOVEMENT_SPEED), 1.25f);
            safeSetBaseValue(attributes.getInstance(ForgeMod.SWIM_SPEED.get()), 1.25f);
            safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_DAMAGE), 4.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.ARMOR), 0.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 0.0f);
            safeSetBaseValue(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE), 0.0f);
            safeSetBaseValue(attributes.getInstance(ForgeMod.BLOCK_REACH.get()), 4.5F);
            safeSetBaseValue(attributes.getInstance(ForgeMod.ENTITY_REACH.get()), 3.0F);
            safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_KNOCKBACK), 0.0f);
        }
    }

    public void refreshEntityAttributes() {
        this.setAttributes(this.getAttributes());
        IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    @Override
    public List<FoodDietEntry> getExtraDietTypes() {
        return LUMINARA_DIET;
    }

    @Override
    public boolean whenPatEvent(LivingEntity patter, InteractionHand hand, LivingEntity patTarget) {
        if (patter.level().isClientSide()) return false;
        patTarget.addEffect(getPatEffect(patter), patter);
        return true;
    }

    @Override
    public boolean whenPattedReaction(LivingEntity patter, InteractionHand hand) {
        if (patter.level().isClientSide()) return false;
        patter.addEffect(getPatEffect(this), this);
        return true;
    }

    @Override
    public float getFlyingSpeed() {
        return super.getFlyingSpeed() * 1.5f;
    }

    private MobEffectInstance getPatEffect(LivingEntity patter) {
        if (!this.isAwakened()) {
            return new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 600);
        }
        if (!patter.isShiftKeyDown()) {
            return new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 600);
        }
        return new MobEffectInstance(ChangedAddonMobEffects.TRANSFUR_SICKNESS.get(), 600, 1);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AWAKENED, false);
        this.entityData.define(HYPER_AWAKENED, false);
        this.entityData.define(COSMIC_WINGS, false);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (AWAKENED.equals(accessor) || HYPER_AWAKENED.equals(accessor)) {
            refreshEntityAttributes();
        }
    }

    public boolean isAwakened() {
        return this.entityData.get(AWAKENED) || isHyperAwakened();
    }

    public void setAwakened(boolean value) {
        this.entityData.set(AWAKENED, value);
    }

    public boolean isHyperAwakened() {
        return this.entityData.get(HYPER_AWAKENED);
    }

    public void setHyperAwakened(boolean value) {
        this.entityData.set(HYPER_AWAKENED, value);
    }

    public boolean hasCosmicWings() {
        return this.entityData.get(COSMIC_WINGS);
    }

    public void setCosmicWings(boolean value) {
        this.entityData.set(COSMIC_WINGS, value);
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);

        // Sky Out-of-Bounds Evolution Check
        if (!this.isAwakened() && this.isFallFlying()) {
            if (this.getY() >= (double) this.level().getMaxBuildHeight()) {
                Player player = this.getUnderlyingPlayer();
                if (player != null) {
                    triggerOutOfBoundsTransformation(player, this);
                }
            }
        }

        if (this.isAwakened() && isHyperAwakened() && spawnParticles) {
            spawnHyperAwakenedParticles();
        }

        AttributeInstance attributeInstance = this.getAttribute(Attributes.FOLLOW_RANGE);
        double range = attributeInstance != null ? attributeInstance.getValue() : 32;
        tryToPacifyNearbyEntities(range * this.getScale());
    }

    private void spawnHyperAwakenedParticles() {
        if (this.random.nextInt(10) == 0) {
            ParticlesUtil.sendParticles(
                    this.level(),
                    ParticleTypes.PORTAL, this.position().add(0, this.getBbHeight() / 2, 0),
                    0.25f, 0.25f, 0.25f,
                    6, 1
            );
        }

        if (this.random.nextInt(20) == 0) {
            ParticlesUtil.sendParticlesWithMotion(
                    this,
                    ParticleTypes.END_ROD,
                    getEyePosition(),
                    new Vec3(0.25, 0.25, 0.25),
                    new Vec3(0, 0.08 + this.random.nextDouble() * 0.05, 0),
                    2, 0.05f
            );
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (isHyperAwakened() && spawnParticles) {
            spawnHyperAwakenedParticles();
        }

        AttributeInstance attributeInstance = this.getAttribute(Attributes.FOLLOW_RANGE);
        double range = attributeInstance != null ? attributeInstance.getValue() : 32;
        tryToPacifyNearbyEntities(range * this.getScale());
    }

    public void tryToPacifyNearbyEntities(double range) {
        List<LivingEntity> nearChangedBeasts = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(range), (entity) -> FoxyasUtil.canEntitySeeOtherIgnoreGlass(entity, this, 90f));
        for (LivingEntity livingEntity : nearChangedBeasts) {
            if (livingEntity instanceof ChangedEntity changedEntity) {
                if (changedEntity.getType().is(ChangedAddonTags.EntityTypes.PACIFY_IMMUNE) || changedEntity instanceof LuminaraFlowerBeastEntity) {
                    continue;
                }

                if (changedEntity.getType().is(ChangedTags.EntityTypes.LATEX)) {
                    if (!changedEntity.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
                        changedEntity.addEffect(new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 60 * 20, 0, true, false, true));
                    }
                }
            } else if (livingEntity instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (instance != null) {
                    if (instance.getChangedEntity().getType().is(ChangedAddonTags.EntityTypes.PACIFY_IMMUNE) || (instance.getChangedEntity() instanceof LuminaraFlowerBeastEntity)) {
                        continue;
                    }

                    if (instance.getParent().getEntityType().is(ChangedTags.EntityTypes.LATEX)) {
                        if (!player.hasEffect(ChangedAddonMobEffects.PACIFIED.get())) {
                            player.addEffect(new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 60 * 20, 0, true, false, true));
                        }
                    }
                }
            }
        }
    }

    @Override
    public CompoundTag savePlayerVariantData() {
        CompoundTag tag = super.savePlayerVariantData();
        this.saveExtraData(tag);
        return tag;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.saveExtraData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readExtraData(tag);
    }

    @Override
    public void readPlayerVariantData(CompoundTag tag) {
        super.readPlayerVariantData(tag);
        this.readExtraData(tag);
    }

    public void saveExtraData(CompoundTag tag) {
        tag.putBoolean("awakened", this.isAwakened());
        tag.putBoolean("hyperAwakened", this.isHyperAwakened());
        tag.putBoolean("cosmicWings", this.hasCosmicWings());
    }

    public void readExtraData(CompoundTag tag) {
        if (tag.contains("awakened")) this.setAwakened(tag.getBoolean("awakened"));
        if (tag.contains("hyperAwakened")) this.setHyperAwakened(tag.getBoolean("hyperAwakened"));
        if (tag.contains("cosmicWings")) this.setCosmicWings(tag.getBoolean("cosmicWings"));
    }

    @Override
    public FlyType getFlyType() {
        return this.isAwakened() ? FlyType.BOTH : FlyType.NONE;
    }

    @Override
    public Color3 getTransfurColor(TransfurCause cause) {
        Color3 firstColor = Color3.getColor("#f5d4ef");
        Color3 secondColor = Color3.getColor("#241942");
        return ColorUtil.lerpTFColor(firstColor, secondColor, getUnderlyingPlayer());
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
            return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(this);
    }

    @Override
    public List<BestiaryInfo> getBestiaryInfo() {
        List<BestiaryInfo> bestiaryInfo = new ArrayList<>(IBestiaryEntityData.super.getBestiaryInfo());
        bestiaryInfo.add(new BestiaryInfo(Component.translatable("text.changed_addon.bestiary.transformation.luminara_flower_beast.title"), Component.translatable("text.changed_addon.bestiary.transformation.luminara_flower_beast.desc"), 2, 0, true));
        return bestiaryInfo;
    }

    @Override
    public BestiaryInfo getBasicLore() {
        return IBestiaryEntityData.super.getBasicLore();
    }

    @Mod.EventBusSubscriber
    public static class TransfurEvolveEventsHandle {

        @SubscribeEvent
        public static void handleDamage(LivingAttackEvent event) {
            if (!(event.getEntity() instanceof Player player) || player.isSpectator()) return;

            TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
            if (instance == null || !instance.is(ChangedAddonTransfurVariants.LUMINARA_FLOWER_BEAST)
                    || !(instance.getChangedEntity() instanceof LuminaraFlowerBeastEntity luminaraFlowerBeast)) return;

            DamageSource source = event.getSource();

            if (source.getDirectEntity() instanceof AreaEffectCloud cloud) {
                LivingEntity shooter = cloud.getOwner();
                if (cloud.getParticle().getType() == ParticleTypes.DRAGON_BREATH
                        && (shooter instanceof LuminaraFlowerBeastEntity || shooter instanceof EnderDragon)) {
                    event.setCanceled(true);
                    return;
                }
            }

            if (!source.is(DamageTypes.FELL_OUT_OF_WORLD)) return;
            boolean isOutOfWorld = player.getY() < (double) (player.level().getMinBuildHeight() - 64);
            if (!isOutOfWorld || event.getAmount() == Float.MAX_VALUE) return;

            triggerVoidTransformation(player, luminaraFlowerBeast);
            event.setCanceled(true);
        }
    }
}