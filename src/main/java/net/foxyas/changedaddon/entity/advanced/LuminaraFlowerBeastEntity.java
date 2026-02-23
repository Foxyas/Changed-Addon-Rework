package net.foxyas.changedaddon.entity.advanced;

import net.foxyas.changedaddon.entity.api.CustomPatReaction;
import net.foxyas.changedaddon.entity.defaults.AbstractBasicOrganicChangedEntity;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.procedure.CreatureDietsHandleProcedure;
import net.foxyas.changedaddon.util.ColorUtil;
import net.foxyas.changedaddon.util.DelayedTask;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.foxyas.changedaddon.variant.VariantExtraStats;
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
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PlayMessages;

import java.util.List;

public class LuminaraFlowerBeastEntity extends AbstractBasicOrganicChangedEntity implements VariantExtraStats, CustomPatReaction, PowderSnowWalkable {

    public static final CreatureDietsHandleProcedure.DietType LUMINARA_DIET = CreatureDietsHandleProcedure.DietType.create("LUMINARA", ChangedAddonTags.TransfurTypes.DRAGON_LIKE, ChangedAddonTags.Items.DRAGON_DIET, List.of(Items.CHORUS_FRUIT, ChangedItems.ORANGE.get()));
    private static final EntityDataAccessor<Boolean> AWAKENED = SynchedEntityData.defineId(LuminaraFlowerBeastEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HYPER_AWAKENED = SynchedEntityData.defineId(LuminaraFlowerBeastEntity.class, EntityDataSerializers.BOOLEAN);
    public boolean spawnParticles = true;
    private boolean attributesApplied;
    private boolean attributesAppliedEntity;

    public LuminaraFlowerBeastEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.LUMINARA_FLOWER_BEAST.get(), world);
    }

    public LuminaraFlowerBeastEntity(EntityType<? extends ChangedEntity> type, Level level) {
        super(type, level);
    }

    /**
     * Main logic for the transformation effect:
     * - Transform player into void form
     * - Launch them upwards
     * - Give potion effects and flight
     * - Spawn explosion-like particles
     */
    private static void triggerVoidTransformation(Player player, LuminaraFlowerBeastEntity luminaraFlowerBeast) {
        if (luminaraFlowerBeast.isHyperAwakened()) return;

        boolean isAwakened = luminaraFlowerBeast.isAwakened();
        if (!isAwakened) luminaraFlowerBeast.setAwakened(true);

        if (tryExtractDragonBreath(player)) {
            luminaraFlowerBeast.setHyperAwakened(true);
            player.level.playSound(null, player, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 5, 1);
            luminaraFlowerBeast.attributesApplied = false;//reset attributesApplied so that HyperAwaken attributes get applied
        } else if (isAwakened) return;

        // Cancel fall/void velocity and launch player upwards
        Vec3 vec = new Vec3(0, 8, 0);
        if (luminaraFlowerBeast.isHyperAwakened()) vec = vec.scale(1.5);
        player.setDeltaMovement(vec); // strong vertical push

        player.hurtMarked = true; // force velocity update to client
        DelayedTask.schedule(20, () -> {
            // Enable flight
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true; // auto fly
            player.onUpdateAbilities();
        });

        // Grant effects
        player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 20 * 10, 1));
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 10, 2));

        // Explosion-like particles
        if (!(player.level instanceof ServerLevel serverLevel)) return;

        float radius = 1f;
        float angle = 22.5f;
        float angleTheta, anglePhi;
        double x, y, z;
        Vec3 pos;
        for (float theta = 0; theta < 360; theta += angle) {
            angleTheta = theta * Mth.DEG_TO_RAD;

            for (float phi = 0; phi <= 180; phi += angle) {
                anglePhi = phi * Mth.DEG_TO_RAD;
                x = player.getX() + Mth.sin(anglePhi) * Mth.cos(angleTheta) * radius;
                y = player.getY() + Mth.cos(anglePhi) * radius;
                z = player.getZ() + Mth.sin(anglePhi) * Mth.sin(angleTheta) * radius;
                pos = new Vec3(x, y, z);
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

    public Vec3 getMouthPosition() {
        return this.getEyePosition().subtract(0, 0.25, 0);
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

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
    }

    public void setAttributesAwakened(AttributeMap attributes) {
        safeSetBaseValue(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()), 5.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.MAX_HEALTH), 40.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.FOLLOW_RANGE), 126.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.MOVEMENT_SPEED), 1.25f);
        safeSetBaseValue(attributes.getInstance(ForgeMod.SWIM_SPEED.get()), 1.25f);
        safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_DAMAGE), 4.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR), 0.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 0.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE), 0.0f);
    }

    public void setAttributesHyperAwakened(AttributeMap attributes) {
        safeSetBaseValue(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get()), 7.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_DAMAGE), 6.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR), 10.0f);
        safeSetBaseValue(attributes.getInstance(Attributes.ARMOR_TOUGHNESS), 6.0f);
        safeSetBaseValue(attributes.getInstance(ForgeMod.BLOCK_REACH.get()), 6.0F);
        safeSetBaseValue(attributes.getInstance(ForgeMod.ENTITY_REACH.get()), 4.0F);
        safeSetBaseValue(attributes.getInstance(Attributes.ATTACK_KNOCKBACK), 2.0f);
    }

    @Override
    public List<CreatureDietsHandleProcedure.DietType> getExtraDietTypes() {
        return List.of(LUMINARA_DIET);
    }

    @Override
    public void WhenPatEvent(LivingEntity patter, InteractionHand hand, LivingEntity patTarget) {
        if (patter.level().isClientSide()) return;

        patTarget.addEffect(getPatEffect(patter), patter);
    }

    @Override
    public void WhenPattedReaction(Player patter, InteractionHand hand) {
        if (patter.level().isClientSide()) return;

        patter.addEffect(getPatEffect(this), this);
    }

    @Override
    public float getFlySpeed() {
        return defaultPlayerFlySpeed * 1.5f;
    }

    private MobEffectInstance getPatEffect(LivingEntity patter) {
        if (!this.isAwakened()) {
            return new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 600);
        }
        if (!patter.isShiftKeyDown()) {
            return new MobEffectInstance(ChangedAddonMobEffects.PACIFIED.get(), 600);
        }
        return new MobEffectInstance(ChangedAddonMobEffects.UNTRANSFUR.get(), 600);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(AWAKENED, false);
        this.entityData.define(HYPER_AWAKENED, false);
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

    /**
     * Apply awakened buffs (stronger, faster on land, but weaker in water).
     */

    public void applyAwakenedBuffs() {
        setAttributesAwakened(getAttributes());
        if (isHyperAwakened()) {
            setAttributesHyperAwakened(getAttributes());
        }
        attributesApplied = true;
        IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    /**
     * Remove awakened buffs, returning entity to neutral state.
     */
    public void removeAwakenedBuffs() {
        setAttributes(getAttributes());
        attributesApplied = false;
        IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        Player player = this.getUnderlyingPlayer();
        if (player != null) {
            if (this.isAwakened()) {
                if (isHyperAwakened()) {
                    if (spawnParticles) {
                        spawnHyperAwakenedParticles();
                    }
                }

                tryToPacifyNearbyEntities(128);
            }
            if (this.isAwakened() && !attributesApplied) {
                applyAwakenedBuffs();
            } else if (!this.isAwakened() && attributesApplied) {
                removeAwakenedBuffs();
            }
        } else {
            AttributeInstance attributeInstance = this.getAttribute(Attributes.FOLLOW_RANGE);
            double range = 32;
            if (attributeInstance != null) {
                range = attributeInstance.getValue();
            }
            tryToPacifyNearbyEntities(range);
        }
    }

    private void spawnHyperAwakenedParticles() {
        if (this.random.nextInt(10) == 0) {
            ParticlesUtil.sendParticles(
                    this.level(),
                    ParticleTypes.PORTAL, this.position().add(0, this.getBbHeight() / 2, 0),
                    0.25f,
                    0.25f,
                    0.25f,
                    6,
                    1
            );
        }

        // End rod - mais altos, tipo energia subindo
        if (this.random.nextInt(20) == 0) {
            ParticlesUtil.sendParticlesWithMotion(
                    this,
                    ParticleTypes.END_ROD,
                    getEyePosition(),
                    new Vec3(0.25, 0.25, 0.25),
                    new Vec3(0, 0.08 + this.random.nextDouble() * 0.05, 0),
                    2,
                    0.05f
            );
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (isHyperAwakened()) {
            if (spawnParticles) {
                spawnHyperAwakenedParticles();
            }
        }

        AttributeInstance attributeInstance = this.getAttribute(Attributes.FOLLOW_RANGE);
        double range = 32;
        if (attributeInstance != null) {
            range = attributeInstance.getValue();
        }
        tryToPacifyNearbyEntities(range);

        if (this.isAwakened() && !attributesAppliedEntity) {
            this.setAttributesAwakened(this.getAttributes());
            if (this.isHyperAwakened()) {
                this.setAttributesHyperAwakened(this.getAttributes());
            }
            this.attributesAppliedEntity = true;
        } else if (!this.isAwakened() && attributesAppliedEntity) {
            this.setAttributes(this.getAttributes());
            this.attributesAppliedEntity = false;
        }

    }

    public void tryToPacifyNearbyEntities(double range) {
        List<LivingEntity> nearChangedBeasts = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(range), (entity) -> FoxyasUtils.canEntitySeeOtherIgnoreGlass(entity, this, 90f));
        for (LivingEntity livingEntity : nearChangedBeasts) {
            if (livingEntity instanceof ChangedEntity changedEntity) {
                if (changedEntity.getType().is(ChangedAddonTags.EntityTypes.PACIFY_IMMUNE)) {
                    continue;
                }

                if (changedEntity instanceof LuminaraFlowerBeastEntity) {
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
                    if (instance.getChangedEntity().getType().is(ChangedAddonTags.EntityTypes.PACIFY_IMMUNE)) {
                        continue;
                    }

                    if ((instance.getChangedEntity() instanceof LuminaraFlowerBeastEntity)) {
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
        tag.putBoolean("Awakened", this.isAwakened());
        tag.putBoolean("HyperAwakened", this.isHyperAwakened());
    }

    public void readExtraData(CompoundTag tag) {
        if (tag.contains("Awakened")) this.setAwakened(tag.getBoolean("Awakened"));
        if (tag.contains("HyperAwakened")) this.setHyperAwakened(tag.getBoolean("HyperAwakened"));
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

    public double getTorsoYOffset(ChangedEntity self) {
        float ageAdjusted = (float) self.tickCount * 0.33333334F * 0.25F * 0.15F;
        float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
        float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
        float bpiSize = (self.getBasicPlayerInfo().getSize(this) - 1.0F) * 2.0F;
        return Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize;
    }

    public double getTorsoYOffsetForFallFly(ChangedEntity self) {
        float bpiSize = (self.getBasicPlayerInfo().getSize(this) - 1.0F) * 2.0F;
        return 0.375 + bpiSize;
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
            return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
        }
        return getTorsoYOffsetForFallFly(this);
    }

    @Mod.EventBusSubscriber
    public static class TransfurEvolveEventsHandle {

        /**
         * Cancel void damage if player is transformed and out of the world.
         * Also cancels dragon breath damage.
         */
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

            // Only cancel OUT_OF_WORLD damage
            if (!source.is(DamageTypes.FELL_OUT_OF_WORLD)) return;
            boolean isOutOfWorld = player.getY() < (double) (player.level.getMinBuildHeight() - 64); // Same
            if (!isOutOfWorld || event.getAmount() == Float.MAX_VALUE) return;

            triggerVoidTransformation(player, luminaraFlowerBeast);

            event.setCanceled(true);
        }
    }
}
