package net.foxyas.changedaddon.entity.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.client.renderer.layers.features.SonarOutlineLayer;
import net.foxyas.changedaddon.client.renderer.renderTypes.ChangedAddonRenderTypes;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.ComboAbilityGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.ComboBurstGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.KnockBackBurstGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.SimpleComboAbilityGoal;
import net.foxyas.changedaddon.entity.ai.goals.void_fox.VoidFoxAntiFlyingAttack;
import net.foxyas.changedaddon.entity.ai.goals.void_fox.VoidFoxDashAttack;
import net.foxyas.changedaddon.entity.api.ICrawlAndSwimAbleEntity;
import net.foxyas.changedaddon.entity.api.IDynamicPawColor;
import net.foxyas.changedaddon.entity.api.IDynamicRideOffsetEntity;
import net.foxyas.changedaddon.entity.api.IHasBossMusic;
import net.foxyas.changedaddon.entity.projectile.AbstractVoidFoxParticleProjectile;
import net.foxyas.changedaddon.entity.projectile.VoidFoxParticleProjectile;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.util.FoxyasUtils;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.EyeStyle;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.Color3;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;

public class VoidFoxEntity extends ChangedEntity implements ICrawlAndSwimAbleEntity, IHasBossMusic, SonarOutlineLayer.CustomSonarRenderable, IDynamicPawColor, IDynamicRideOffsetEntity {

    private static final int MAX_1_COOLDOWN = 120;
    private static final int MAX_2_COOLDOWN = 120;
    private static final int MAX_COOLDOWN = 120;

    private static final EntityDataAccessor<Float> DODGE_HEALTH = SynchedEntityData.defineId(VoidFoxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MAX_DODGE_HEALTH = SynchedEntityData.defineId(VoidFoxEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IS_BOSS = SynchedEntityData.defineId(VoidFoxEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WAS_BOSS = SynchedEntityData.defineId(VoidFoxEntity.class, EntityDataSerializers.BOOLEAN);

    public final ServerBossEvent bossBar = getBossBar();
    public final ServerBossEvent dodgeHealthBossBar = getDodgeHealthBossBar();

    public int timesUsedAttack1, timesUsedAttack2, timesUsedAttack3, timesUsedAttack4/*, timesUsedAttack5*/ = 0;
    public int stunTicks = 0;
    private int Attack1Cooldown, Attack2Cooldown, Attack3Cooldown, Attack4Cooldown/*, Attack5Cooldown*/;
    private int AttackInUse;
    private int ticksInUse;
    private int ticksTakeDmgFromFire = 0;

    public VoidFoxEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.VOID_FOX.get(), world);
    }

    public VoidFoxEntity(EntityType<VoidFoxEntity> type, Level world) {
        super(type, world);
        xpReward = 1000;
        setNoAi(false);
        setPersistenceRequired();
    }

    public static int getMaxCooldown() {
        return MAX_COOLDOWN;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder = builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 3f);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 2);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    public boolean isBoss() {
        return this.entityData.get(IS_BOSS);
    }

    public void setBoss(boolean boss) {
        this.entityData.set(IS_BOSS, boss);
    }

    public boolean wasBoss() {
        return this.entityData.get(WAS_BOSS);
    }

    public void setWasBoss(boolean value) {
        this.entityData.set(WAS_BOSS, value);
    }

    public void refreshBossAttributes() {
        if (!wasBoss() && isBoss()) {
            handleBoss();
        } else if (wasBoss() && !isBoss()) {
            handleNonBoss();
        }
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);

        if (pKey == WAS_BOSS) {
            refreshBossAttributes();
        }
    }

    @Override
    public IDynamicPawColor.PawStyle getPawStyle() {
        return IDynamicPawColor.PawStyle.FERAL;
    }

    @Override
    public Color getPawBeansColor() {
        return Color.WHITE;
    }

    @Override
    public Color getPawColor() {
        return Color.BLACK;
    }

    public boolean causeFallDamage(float p_148859_, float p_148860_, @NotNull DamageSource p_148861_) {
        return false;
    }

    public ServerBossEvent getBossBar() {
        var bossBar = new ServerBossEvent(
                this.getDisplayName(), // Nome exibido na boss bar
                BossEvent.BossBarColor.WHITE, // Cor da barra
                BossEvent.BossBarOverlay.NOTCHED_10 // Estilo da barra
        );
        bossBar.setCreateWorldFog(true);
        bossBar.setDarkenScreen(true);
        return bossBar;
    }

    public ServerBossEvent getDodgeHealthBossBar() {
        var bossBar = new ServerBossEvent(
                this.getDisplayName(), // Nome exibido na boss bar
                BossEvent.BossBarColor.WHITE, // Cor da barra
                BossEvent.BossBarOverlay.NOTCHED_12 // Estilo da barra
        );
        bossBar.setCreateWorldFog(true);
        bossBar.setDarkenScreen(true);
        return bossBar;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MAX_DODGE_HEALTH, 200f);
        this.entityData.define(DODGE_HEALTH, getMaxDodgeHealth());
        this.entityData.define(IS_BOSS, false);
        this.entityData.define(WAS_BOSS, false);
    }


    @Override
    public TransfurMode getTransfurMode() {
        if (this.getTarget() != null) {
            return TransfurMode.NONE;
        }

        return TransfurMode.ABSORPTION;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void RegisterHit() {
        this.goalSelector.getAvailableGoals().forEach((wrappedGoal -> {
            if (wrappedGoal.getGoal() instanceof KnockBackBurstGoal knockBackBurstGoal) {
                knockBackBurstGoal.registerHit();
            }
        }));
    }

    public void RegisterDamage(float amount) {
        this.goalSelector.getAvailableGoals().forEach((wrappedGoal -> {
            if (wrappedGoal.getGoal() instanceof KnockBackBurstGoal knockBackBurstGoal) {
                knockBackBurstGoal.registerDamage(amount);
            }
        }));
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        TransfurVariantInstance<?> transfurVariant = ProcessTransfur.getPlayerTransfurVariant(EntityUtil.playerOrNull(this.getUnderlyingPlayer()));
        if (transfurVariant != null) {
            DodgeAbilityInstance abilityInstance = transfurVariant.getAbilityInstance(ChangedAddonAbilities.DODGE.get());
            if (abilityInstance != null) {
                abilityInstance.setMaxDodgeAmount(10);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(10, new VoidFoxAntiFlyingAttack(this, 0, 32f, 1f, 40) {
            @Override
            public void start() {
                super.start();
                if (getTarget() instanceof Player player) {
                    player.displayClientMessage(Component.literal("Flying will not help you").withStyle((style -> {
                        Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                        returnStyle = returnStyle.withItalic(true);
                        return returnStyle;
                    })), true);
                }
            }
        });
        this.goalSelector.addGoal(15, new ComboBurstGoal(this, 10, 10));
        this.goalSelector.addGoal(5, new VoidFoxDashAttack(this, ChangedAddonEntities.PARTICLE_PROJECTILE.get()) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack2Cooldown() < VoidFoxEntity.MAX_1_COOLDOWN) {
                    return false;
                }
                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 1) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack1 >= 2) {
                    return false;
                }

                return super.canUse();
            }

            @Override
            public void start() {
                super.start();
                if (getTarget() instanceof Player player) {
                    player.displayClientMessage(Component.literal("Here i go!").withStyle((style -> {
                        Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                        returnStyle = returnStyle.withItalic(true);
                        return returnStyle;
                    })), true);
                }
                VoidFoxEntity.this.setAttack2Cooldown(0);
                VoidFoxEntity.this.timesUsedAttack1 += 1;
                VoidFoxEntity.this.timesUsedAttack2 = 0;
                VoidFoxEntity.this.timesUsedAttack3 = 0;
                VoidFoxEntity.this.timesUsedAttack4 = 0;
                //VoidFoxEntity.this.timesUsedAttack5 = 0;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 1;
                super.tick();
            }

            @Override
            public void stop() {
                super.stop();
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });

        this.goalSelector.addGoal(4, new ComboAbilityGoal(
                this, 3f, 18f, 3f, 5,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_STRONG,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.EXPLOSION, ParticleTypes.FLASH}
        ) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack1Cooldown() < VoidFoxEntity.getMaxCooldown()) {
                    return false;
                }

                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 2) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack2 >= 2) {
                    return false;
                }

                if (VoidFoxEntity.this.getRandom().nextFloat() >= 0.25f) {
                    return false;
                }


                return super.canUse();
            }

            @Override
            public void start() {
                super.start();
                if (getTarget() instanceof Player player) {
                    player.displayClientMessage(Component.literal("Lest DANCE").withStyle((style -> {
                        Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                        returnStyle = returnStyle.withItalic(true);
                        return returnStyle;
                    })), true);
                }
                VoidFoxEntity.this.setAttack1Cooldown(0);
                VoidFoxEntity.this.timesUsedAttack1 = 0;
                VoidFoxEntity.this.timesUsedAttack2 += 1;
                VoidFoxEntity.this.timesUsedAttack3 = 0;
                VoidFoxEntity.this.timesUsedAttack4 = 0;
                //VoidFoxEntity.this.timesUsedAttack5 = 0;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 2;
                super.tick();
            }

            @Override
            public void stop() {
                if (this.isShouldEnd()) {
                    if (getTarget() != null) {
                        FoxyasUtils.repairArmor(getTarget(), 25);
                    }
                    if (getTarget() instanceof Player player) {
                        player.displayClientMessage(Component.literal("Heh nice one").withStyle((style -> {
                            Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                            returnStyle = returnStyle.withItalic(true);
                            return returnStyle;
                        })), true);
                        {
                            VoidFoxEntity.this.stunTicks = 120;
                        }
                    }
                }
                super.stop();
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });

        this.goalSelector.addGoal(4, new SimpleComboAbilityGoal(
                this, 2, 3f, 18f, 3f, 5,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_STRONG,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.EXPLOSION, ParticleTypes.FLASH}
        ) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack3Cooldown() < VoidFoxEntity.getMaxCooldown()) {
                    return false;
                }

                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 3) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack3 >= 4) {
                    return false;
                }

                if (VoidFoxEntity.this.getRandom().nextFloat() >= 0.70f) {
                    return false;
                }


                return super.canUse();
            }

            @Override
            public void start() {
                super.start();
                if (getTarget() instanceof Player player) {
                    player.displayClientMessage(Component.literal("can you keep up with me?").withStyle((style -> {
                        Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                        returnStyle = returnStyle.withItalic(true);
                        return returnStyle;
                    })), true);
                }
                VoidFoxEntity.this.setAttack3Cooldown(0);
                VoidFoxEntity.this.timesUsedAttack1 = 0;
                VoidFoxEntity.this.timesUsedAttack2 = 0;
                VoidFoxEntity.this.timesUsedAttack3 += 1;
                VoidFoxEntity.this.timesUsedAttack4 = 0;
                //VoidFoxEntity.this.timesUsedAttack5 = 0;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 3;
                super.tick();
            }

            @Override
            public void stop() {
                if (this.isShouldEnd()) {
                    if (getTarget() != null) {
                        FoxyasUtils.repairArmor(getTarget(), 25);
                    }
                    if (getTarget() instanceof Player player) {
                        player.displayClientMessage(Component.literal("Heh it seems so").withStyle((style -> {
                            Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                            returnStyle = returnStyle.withItalic(true);
                            return returnStyle;
                        })), true);
                        {
                            VoidFoxEntity.this.stunTicks = 120;
                        }
                    }
                }
                super.stop();
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });


        this.goalSelector.addGoal(4, new SimpleComboAbilityGoal(
                this, 3, 3f, 18f, 3f, 2,
                new SoundEvent[]{SoundEvents.PLAYER_ATTACK_SWEEP,
                        SoundEvents.PLAYER_ATTACK_STRONG,
                        SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundEvents.LIGHTNING_BOLT_THUNDER},
                new ParticleOptions[]{ParticleTypes.FLASH, ParticleTypes.EXPLOSION, ParticleTypes.FLASH}
        ) {
            @Override
            public boolean canUse() {
                if (VoidFoxEntity.this.getAttack4Cooldown() < VoidFoxEntity.getMaxCooldown()) {
                    return false;
                }

                if (VoidFoxEntity.this.AttackInUse > 0 && VoidFoxEntity.this.AttackInUse != 4) {
                    return false;
                }

                if (VoidFoxEntity.this.timesUsedAttack4 >= 4) {
                    return false;
                }

                if (VoidFoxEntity.this.getRandom().nextFloat() >= 0.5) {
                    return false;
                }


                return super.canUse();
            }

            @Override
            public void start() {
                if (getTarget() instanceof Player player) {
                    player.displayClientMessage(Component.literal("can you keep up with me like this?").withStyle((style -> {
                        Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                        returnStyle = returnStyle.withItalic(true);
                        return returnStyle;
                    })), true);
                }
                super.start();
                VoidFoxEntity.this.setAttack4Cooldown(0);
                VoidFoxEntity.this.timesUsedAttack1 = 0;
                VoidFoxEntity.this.timesUsedAttack2 = 0;
                VoidFoxEntity.this.timesUsedAttack3 = 0;
                VoidFoxEntity.this.timesUsedAttack4 += 1;
                //VoidFoxEntity.this.timesUsedAttack5 = 0;
            }

            @Override
            public void tick() {
                VoidFoxEntity.this.AttackInUse = 4;
                super.tick();
            }

            @Override
            public void stop() {
                if (this.isShouldEnd()) {
                    if (getTarget() != null) {
                        FoxyasUtils.repairArmor(getTarget(), 25);
                    }
                    if (getTarget() instanceof Player player) {
                        player.displayClientMessage(Component.literal("Wow yeah it seems so").withStyle((style -> {
                            Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                            returnStyle = returnStyle.withItalic(true);
                            return returnStyle;
                        })), true);
                        {
                            VoidFoxEntity.this.stunTicks = 120;
                        }
                    }
                }
                super.stop();
                VoidFoxEntity.this.AttackInUse = 0;
            }
        });


    }

    public int getAttack1Cooldown() {
        return Attack1Cooldown;
    }

    public void setAttack1Cooldown(int attack1Cooldown) {
        Attack1Cooldown = attack1Cooldown;
    }

    public int getAttack2Cooldown() {
        return Attack2Cooldown;
    }

    public void setAttack2Cooldown(int attack2Cooldown) {
        Attack2Cooldown = attack2Cooldown;
    }

    public int getAttack3Cooldown() {
        return Attack3Cooldown;
    }

    public void setAttack3Cooldown(int attack3Cooldown) {
        Attack3Cooldown = attack3Cooldown;
    }

    public int getAttack4Cooldown() {
        return Attack4Cooldown;
    }

    public void setAttack4Cooldown(int attack4Cooldown) {
        Attack4Cooldown = attack4Cooldown;
    }

    public Color3 getDripColor() {
        return this.getRandom().nextBoolean() ? Color3.BLACK : Color3.WHITE;
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

    public void doClawsAttackEffect() {// Efeito visual
        double d0 = (double) (-Mth.sin(this.getYRot() * 0.017453292F)) * 1;
        double d1 = (double) Mth.cos(this.getYRot() * 0.017453292F) * 1;
        if (this.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d0, this.getY(0.5), this.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d0, this.getY(0.6), this.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, this.getX() + d0, this.getY(0.7), this.getZ() + d1, 0, d0, 0.0, d1, 0.0);
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1f, 0.75f);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
            return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
        }

        return getTorsoYOffsetForFallFly(this);
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return super.targetSelectorTest(livingEntity) || livingEntity instanceof ChangedEntity;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("dodgeHealth")) this.setDodgeHealth(tag.getFloat("dodgeHealth"));
        if (tag.contains("maxDodgeHealth")) this.setMaxDodgeHealth(tag.getFloat("maxDodgeHealth"));

        if (tag.contains("AttacksHandle")) {
            CompoundTag attackTag = tag.getCompound("AttacksHandle");

            if (attackTag.contains("Attack1Cooldown")) this.Attack1Cooldown = attackTag.getInt("Attack1Cooldown");
            if (attackTag.contains("Attack2Cooldown")) this.Attack2Cooldown = attackTag.getInt("Attack2Cooldown");
            if (attackTag.contains("Attack3Cooldown")) this.Attack3Cooldown = attackTag.getInt("Attack3Cooldown");
            if (attackTag.contains("Attack4Cooldown")) this.Attack4Cooldown = attackTag.getInt("Attack4Cooldown");
            //if (attackTag.contains("Attack5Cooldown")) this.Attack5Cooldown = attackTag.getInt("Attack5Cooldown");
            if (attackTag.contains("AttackInUse")) this.AttackInUse = attackTag.getInt("AttackInUse");

            if (attackTag.contains("timeUsedAttack1")) this.timesUsedAttack1 = attackTag.getInt("timeUsedAttack1");
            if (attackTag.contains("timeUsedAttack2")) this.timesUsedAttack2 = attackTag.getInt("timeUsedAttack2");
            if (attackTag.contains("timeUsedAttack3")) this.timesUsedAttack3 = attackTag.getInt("timeUsedAttack3");
            if (attackTag.contains("timeUsedAttack4")) this.timesUsedAttack4 = attackTag.getInt("timeUsedAttack4");
            //if (attackTag.contains("timeUsedAttack5")) this.timesUsedAttack5 = attackTag.getInt("timeUsedAttack5");
        }

        setBoss(tag.getBoolean("isBoss"));
        if (tag.contains("wasBoss")) this.setWasBoss(tag.getBoolean("wasBoss"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        //tag.putInt("dodgeTicks", this.getDodgingTicks());
        tag.putFloat("dodgeHealth", this.getDodgeHealth());
        tag.putFloat("maxDodgeHealth", this.getMaxDodgeHealth());

        CompoundTag attackTag = new CompoundTag();
        attackTag.putInt("Attack1Cooldown", this.Attack1Cooldown);
        attackTag.putInt("Attack2Cooldown", this.Attack2Cooldown);
        attackTag.putInt("Attack3Cooldown", this.Attack3Cooldown);
        attackTag.putInt("Attack4Cooldown", this.Attack4Cooldown);
        //attackTag.putInt("Attack5Cooldown", this.Attack5Cooldown);
        attackTag.putInt("AttackInUse", this.AttackInUse);
        attackTag.putInt("timeUsedAttack1", this.timesUsedAttack1);
        attackTag.putInt("timeUsedAttack2", this.timesUsedAttack2);
        attackTag.putInt("timeUsedAttack3", this.timesUsedAttack3);
        attackTag.putInt("timeUsedAttack4", this.timesUsedAttack4);
        //attackTag.putInt("timeUsedAttack5", this.timesUsedAttack5);

        tag.put("AttacksHandle", attackTag);

        tag.putBoolean("wasBoss", this.wasBoss());
        if (isBoss()) tag.putBoolean("isBoss", true);
    }

    @Override
    public void readPlayerVariantData(CompoundTag tag) {
        super.readPlayerVariantData(tag);
        setBoss(tag.getBoolean("isBoss"));
    }

    @Override
    public CompoundTag savePlayerVariantData() {
        CompoundTag tag = super.savePlayerVariantData();
        if (isBoss()) tag.putBoolean("isBoss", true);
        return tag;
    }

    public float getDodgeHealth() {
        return this.entityData.get(DODGE_HEALTH);
    }

    public void setDodgeHealth(float value) {
        this.entityData.set(DODGE_HEALTH, value);
    }

    public void subDodgeHealth(float value) {
        float pValue = Math.max(this.getDodgeHealth() - value, 0);
        this.entityData.set(DODGE_HEALTH, pValue);
    }

    public void addDodgeHealth(float value) {
        this.entityData.set(DODGE_HEALTH, this.getDodgeHealth() + value);
    }

    public float getMaxDodgeHealth() {
        return this.entityData.get(MAX_DODGE_HEALTH);
    }

    public void setMaxDodgeHealth(float maxDodgeHealth) {
        this.entityData.set(MAX_DODGE_HEALTH, maxDodgeHealth);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean willHit = this.getDodgeHealth() - amount <= 0;

        if (source.getEntity() instanceof AbstractVoidFoxParticleProjectile
                || source.getDirectEntity() instanceof AbstractVoidFoxParticleProjectile) {
            boolean f = super.hurt(source, amount * 3.5f);
            this.invulnerableTime = 0;
            return f;
        }

        if (source.is(DamageTypeTags.IS_FIRE)) {
            ticksTakeDmgFromFire++;
        }

        if (source.getEntity() != null) {
            if (VoidFoxEntity.this.AttackInUse > 0) {
                if (VoidFoxEntity.this.AttackInUse != 1) {
                    return super.hurt(source, amount);
                } else {
                    if (!isMoreOp()) {
                        this.goalSelector.getRunningGoals().forEach((wrappedGoal -> {
                            if (wrappedGoal.getGoal() instanceof VoidFoxDashAttack dashAttack) {
                                if (dashAttack.isChargingDash()) {
                                    dashAttack.setTickCount(dashAttack.getTickCount() + 5);
                                }
                            }
                        }));
                    } else {
                        this.goalSelector.getRunningGoals().forEach((wrappedGoal -> {
                            if (wrappedGoal.getGoal() instanceof VoidFoxDashAttack dashAttack) {
                                if (dashAttack.isChargingDash()) {
                                    dashAttack.setTickCount(dashAttack.getTickCount() + 1);
                                }
                            }
                        }));
                        return super.hurt(source, amount);
                    }
                }

            }

            if (!willHit) {
                this.setDodging(source.getEntity());
                this.hurtDodgeHealth(source, amount);
                return false;
            } else {
                this.hurtDodgeHealth(source, amount);
                if (getDodgeHealth() > 0) this.setDodgeHealth(0);
                this.RegisterDamage(amount);
                //this.setDodging(source.getEntity());
                return super.hurt(source, amount);
            }

        }

        if (source.getDirectEntity() != null &&
                ForgeRegistries.ENTITY_TYPES.getKey(source.getDirectEntity().getType()) != null) {
            String id = ForgeRegistries.ENTITY_TYPES.getKey(source.getDirectEntity().getType()).toString();
            if (id.contains("bullet") || id.contains("gun")) {
                this.RegisterDamage(amount);
                this.setDodging(source.getEntity());
                return false;
            }
        }

        return super.hurt(source, amount);
    }

    @Override
    public void die(@NotNull DamageSource pDamageSource) {
        if (pDamageSource.getEntity() instanceof LivingEntity living) {
            FoxyasUtils.repairAllItems(living, 1000);
        }

        super.die(pDamageSource);
    }

    public boolean hurtDodgeHealth(@NotNull DamageSource damageSource, float damageAmount) {
        if (damageSource.is(DamageTypeTags.IS_FIRE)) return false;

        // Apply normal mitigations
        damageAmount = this.getDamageAfterArmorAbsorb(damageSource, damageAmount);
        damageAmount = this.getDamageAfterMagicAbsorb(damageSource, damageAmount);

        // Subtract from dodge health
        this.subDodgeHealth(damageAmount);

        // Counter-attack trigger: solvent projectile
        if (damageSource.is(DamageTypeTags.IS_PROJECTILE) && damageSource.getMsgId().contains(ChangedAddonDamageSources.LATEX_SOLVENT.source(this.level()).getMsgId())) {
            Entity attacker = damageSource.getDirectEntity();

            if (attacker != null) {

                /* =======================================================
                 * 1) Teleport behind the attacker (or fallback into them)
                 * ======================================================= */

                // Vector pointing BEHIND the attacker
                Vec3 behind = attacker.getViewVector(0).scale(-0.5);

                boolean teleportedBehind = this.randomTeleport(
                        attacker.getX() + behind.x,
                        attacker.getY(),
                        attacker.getZ() + behind.z,
                        true
                );

                if (!teleportedBehind) {
                    // Fallback: teleport in front of the attacker
                    Vec3 inFront = attacker.getViewVector(0).scale(0.25);
                    boolean teleportedInFront = this.randomTeleport(
                            attacker.getX() + inFront.x,
                            attacker.getY(),
                            attacker.getZ() + inFront.z,
                            true
                    );

                    // Fallback: teleport directly on top of the attacker
                    if (!teleportedInFront) {
                        Vec3 pos = attacker.position();
                        this.teleportToWithTicket(pos.x, pos.y, pos.z);
                    }
                }

                /* =======================================================
                 * 2) Apply a knockback burst to the attacker
                 * ======================================================= */

                // Direction: mob → attacker
                double dx = attacker.getX() - this.getX();
                double dy = attacker.getY() - this.getY();
                double dz = attacker.getZ() - this.getZ();
                double distance = Math.max(0.2, Math.sqrt(dx * dx + dy * dy + dz * dz));

                double force = 1.25; // knockback force

                attacker.push(
                        (dx / distance) * force,
                        (dy / distance) * force,
                        (dz / distance) * force
                );

                attacker.hurtMarked = true; // sync movement with the client

                // Apply cooldown to the player's item
                if (attacker instanceof Player player) {
                    final Item laethinminator = ChangedAddonItems.LAETHINMINATOR.get();
                    if (player.getUseItem().is(laethinminator)) {
                        player.getCooldowns().addCooldown(laethinminator, 600);
                        player.stopUsingItem();
                    }
                }
            }
        }

        // Register dodge damage in combat tracker
        this.getCombatTracker().recordDamage(damageSource, damageAmount);
        return true;
    }


    private void setDodging(Entity entity) {
        if (entity != null) {
            this.getLookControl().setLookAt(entity.getEyePosition());
        }
        this.getNavigation().stop();
        DodgeAbilityInstance.executeRandomDodgeAnimation(this);
    }

    public void tickAttackTicks() {
        if (isNoAi()) return;

        if (this.stunTicks > 0) {
            this.stunTicks--;
            return;
        }
        if (AttackInUse != 0) {
            ticksInUse++;
            if (ticksInUse > 260) {
                AttackInUse = 0;
                ticksInUse = 0;
            }
            return;
        }
        int value = isMoreOp() ? 3 : 1;

        if (this.Attack1Cooldown < MAX_COOLDOWN) {
            float delay = isMoreOp() ? 2 : 5;
            if (this.tickCount % delay == 1) {
                this.Attack1Cooldown++;
            }
        }
        if (this.Attack2Cooldown < MAX_1_COOLDOWN) {
            this.Attack2Cooldown += value;
        }
        if (this.Attack3Cooldown < MAX_2_COOLDOWN) {
            this.Attack3Cooldown += value;
        }
        if (this.Attack4Cooldown < MAX_COOLDOWN) {
            this.Attack4Cooldown += value;
        }
    }

    public boolean isMoreOp() {
        return this.getDodgeHealth() <= 0;
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
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        crawlingSystem((float) this.getAttributeValue(ForgeMod.SWIM_SPEED.get()) * 0.35f);
        tickAttackTicks();

        handleChanges();

        if (level.isClientSide) return;

        if (ticksTakeDmgFromFire > 5) {
            ticksTakeDmgFromFire = 0;
            this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.HOSTILE, 1.0F, 1.0F);
            if (this.level instanceof ServerLevel server) {
                server.sendParticles(
                        ParticleTypes.EXPLOSION_EMITTER,
                        this.getX(), this.getEyeY(), this.getZ(),
                        1, 0, 0, 0, 0
                );
            }
            for (LivingEntity living : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8), (livingEntity -> !livingEntity.isSpectator() && !livingEntity.is(this)))) {
                Vec3 knock = living.position().subtract(this.position()).normalize().scale(1.2);
                living.push(knock.x, knock.y * 1.25f, knock.z);
                if (living instanceof Player player) {
                    player.displayClientMessage(Component.literal("ENOUGH OF THIS").withStyle((style -> {
                        Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                        returnStyle = returnStyle.withItalic(true);
                        return returnStyle;
                    })), true);
                }
            }
            this.level().playSound(null, this.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, 1.0F, 1.0F);
            this.setRemainingFireTicks(0);
            // int totalProjectiles = 12; // número de projéteis
            double radius = 1.5;

            for (int theta = 0; theta < 360; theta += 45) { // Ângulo horizontal (longitude)
                double angleTheta = Math.toRadians(theta);
                for (int phi = 0; phi <= 180; phi += 45) { // Ângulo vertical (latitude)
                    double anglePhi = Math.toRadians(phi);

                    // Direção do disparo (coordenadas cartesianas de uma esfera)
                    double dx = Math.sin(anglePhi) * Math.cos(angleTheta);
                    double dy = Math.cos(anglePhi);
                    double dz = Math.sin(anglePhi) * Math.sin(angleTheta);

                    // Posição inicial (esfera ao redor da entidade)
                    double px = this.getX() + dx * radius;
                    double py = this.getY() + dy * radius + 1.0; // leve ajuste de altura
                    double pz = this.getZ() + dz * radius;

                    VoidFoxParticleProjectile projectile = new VoidFoxParticleProjectile(ChangedAddonEntities.PARTICLE_PROJECTILE.get(), this.level);
                    projectile.setSmoothMotion(true);
                    projectile.setPos(px, py, pz);
                    projectile.shoot(dx, dy, dz, 1.0f, 0.0f); // dispara na direção da esfera
                    projectile.setOwner(this);
                    projectile.setTarget(this.getTarget());
                    projectile.setParryAble(true);

                    this.level.addFreshEntity(projectile);
                }
            }

        }
        if (this.getDodgeHealth() > 0) {
            this.dodgeHealthBossBar.setVisible(true);
            //this.bossBar.setVisible(false);
            this.dodgeHealthBossBar.setProgress(this.getDodgeHealth() / this.getMaxDodgeHealth());
            this.dodgeHealthBossBar.setOverlay(BossEvent.BossBarOverlay.NOTCHED_12);
        } else {
            this.dodgeHealthBossBar.setVisible(false);
        }

        if (this.getDodgeHealth() <= 0 || this.computeHealthRatio() < 1) {
            this.bossBar.setVisible(true);
            //this.dodgeHealthBossBar.setVisible(false);
            this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
            this.bossBar.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
        } else {
            this.bossBar.setVisible(false);
        }
    }

    private void handleChanges() {
        this.goalSelector.getRunningGoals().forEach((wrappedGoal -> {
            if (wrappedGoal.getGoal() instanceof VoidFoxDashAttack dashAttack) {
                if (this.isMoreOp()) {
                    if (dashAttack.isChargingDash()) {
                        dashAttack.setDashSpeed(2.5f);
                    }
                }
            }
        }));

        if (this.isMoreOp()) {
            float value = getRandom().nextFloat() + 0.25f;
            if (!((value + getHealth()) / this.getMaxHealth() > 0.5f)) {
                if ((this.hurtTime <= 0) && this.tickCount % 10 == 0) {
                    this.heal(value);
                }
            }
        }

    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (!isBoss()) return;

        this.dodgeHealthBossBar.addPlayer(player);
        this.bossBar.addPlayer(player);

        // Mensagem atmosférica
        MutableComponent chatComponent = Component.literal(
                "A chill runs down your spine...\n" +
                        "Something is watching."
        ).withStyle(style -> style
                .withColor(ChatFormatting.DARK_GRAY)
                .withItalic(true)
        );

        player.displayClientMessage(
                chatComponent,
                false
        );
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (!isBoss()) return;

        this.bossBar.removePlayer(player);
        this.dodgeHealthBossBar.removePlayer(player);
    }

    @Override
    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((7.5));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((60f));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.25f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.25f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(6);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(6);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0.5);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(3);
        attributes.getInstance(ChangedAttributes.AIR_CAPACITY.get()).setBaseValue(15);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.1);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor p_21434_, @NotNull DifficultyInstance p_21435_, @NotNull MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag tag) {
        if ((tag != null)) {
            setBoss(tag.getBoolean("isBoss"));
        }

        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, tag);
    }

    public void handleBoss() {
        //this.setAbsorptionAmount(75f);
        Objects.requireNonNull(this.getAttribute(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((7.5));
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue((500));
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.1);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.1);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(10);
        this.getAttribute(Attributes.ARMOR).setBaseValue(20);
        this.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(12);
        this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue(2);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        if (!wasBoss()) {
            this.setHealth(this.getMaxHealth());
            this.setDodgeHealth(this.getMaxDodgeHealth());
            setWasBoss(true);
            IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
        }
    }

    public void handleNonBoss() {
        this.setAttributes(this.getAttributes());
        if (!wasBoss()) return;

        this.setHealth(this.getMaxHealth());
        this.setMaxDodgeHealth(3);
        setWasBoss(false);
        IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    // Don't know why but getId do not work fine with the BossMusicHandler
    @Override
    public ResourceLocation getBossMusic() {
        if (!isBoss()) return null;

        if (this.isMoreOp()) {
            return ChangedAddonSoundEvents.EXP10_THEME.get().getLocation();
        }

        return ChangedAddonSoundEvents.EXP9_THEME.get().getLocation();
    }

    @Override
    public LivingEntity getSelf() {
        return this;
    }

    public int getAttackInUse() {
        return AttackInUse;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean handleSonarRender(@NotNull SonarOutlineLayer<?, ?> sonarOutlineLayer, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float alpha) {
        // Default colors: white
        float r = 1.0f, g = 1.0f, b = 1.0f;

        Minecraft minecraft = Minecraft.getInstance();
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        EntityRenderer<? super VoidFoxEntity> renderer = entityRenderDispatcher.getRenderer(this);
        RenderType outline = ChangedAddonRenderTypes.outlineWithTranslucencyCull(renderer.getTextureLocation(this));
        sonarOutlineLayer.getParentModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(outline),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                r, g, b, alpha
        );
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean handleSonarRenderForCamera(@NotNull SonarOutlineLayer<?, ?> sonarOutlineLayer, @NotNull LivingEntity livingEntity, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float alpha) {
        // Default colors: white
        float r = 1.0f, g = 1, b = 1;

        Minecraft minecraft = Minecraft.getInstance();
        EntityRenderDispatcher entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity> renderer = entityRenderDispatcher.getRenderer(livingEntity);
        RenderType outline = ChangedAddonRenderTypes.outlineWithTranslucencyCull(renderer.getTextureLocation(livingEntity));
        sonarOutlineLayer.getParentModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(outline),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                r, g, b, alpha
        );
        return true;
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {

        @SubscribeEvent
        public static void WhenAttack(LivingAttackEvent event) {
            Entity source = event.getSource().getEntity();
            if (source instanceof VoidFoxEntity voidFoxEntity) {
                voidFoxEntity.RegisterHit();
                return;
            }

            LivingEntity target = event.getEntity();
            if (!(target instanceof VoidFoxEntity voidFox)) return;

            if (source != null && voidFox.computeHealthRatio() > 0.5f) {
                if (((voidFox.getHealth() - event.getAmount()) / voidFox.getMaxHealth()) <= 0.5f) {
                    if (source instanceof Player player) {
                        player.displayClientMessage(Component.literal("I will hasten the arrival of your death").withStyle((style -> {
                            Style returnStyle = style.withColor(ChatFormatting.DARK_GRAY);
                            returnStyle = returnStyle.withItalic(true);
                            return returnStyle;
                        })), true);
                    }
                }
            }
        }
    }
}
