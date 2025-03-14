package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.CustomHandle.BossAbilitiesHandle;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.registers.ChangedAddonDamageSources;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.EyeStyle;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import net.minecraft.sounds.SoundEvents;

public abstract class AbstractLuminarcticLeopard extends AbstractSnowLeopard {

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {
        @SubscribeEvent
        public static void WhenAttack(LivingAttackEvent event) {
            LivingEntity target = event.getEntityLiving();
            Entity source = event.getSource().getEntity();
            if (source instanceof AbstractLuminarcticLeopard lumi && lumi.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                target.setTicksFrozen(target.getTicksFrozen() + (int) (target.getTicksRequiredToFreeze() * 0.25f));
            } else if (source instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
                        && instance != null
                        && instance.getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.CAUSE_FREEZE_DMG)) {
                    target.setTicksFrozen(target.getTicksFrozen() + (int) (target.getTicksRequiredToFreeze() * 0.25f));
                    target.playSound(SoundEvents.PLAYER_HURT_FREEZE, 2f, 1f);
                }
            }
        }
    }

    public final ServerBossEvent bossBar = new ServerBossEvent(
            this.getDisplayName(), // Nome exibido na boss bar
            BossEvent.BossBarColor.WHITE, // Cor da barra
            BossEvent.BossBarOverlay.NOTCHED_6 // Estilo da barra
    );

    boolean ActivatedAbility = false;
    public float AbilitiesTicksCooldown = 20;
    public int SuperAbilitiesTicksCooldown = 0;
    public int PassivesTicksCooldown = 0;
    public int DashingTicks = 0;
    public final BossAbilitiesHandle bossAbilitiesHandle = new BossAbilitiesHandle(this);
    public int DodgeAnimTicks = 0;
    public final int DodgeAnimMaxTicks = 20;

    //public int DEVATTACKTESTTICK = 0;
    public AbstractLuminarcticLeopard(EntityType<? extends AbstractSnowLeopard> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    public int GlowStage = 0; //0 = normal and 1 = Pulse

    public boolean isActivatedAbility() {
        return ActivatedAbility;
    }

    public void SetActivatedAbility(boolean value) {
        this.ActivatedAbility = value;
    }

    @Override
    public void setTarget(@Nullable LivingEntity entity) {
        super.setTarget(entity);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getUnderlyingPlayer() == null) {
            if (!this.level.isClientSide) {
                this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
            }

		/*if (this.DEVATTACKTESTTICK != 0){
			this.AbilitiesTicksCooldown = 0;
			this.ActivatedAbility = true;
		}*/

            if (!this.isNoAi()) {
                if (this.DodgeAnimTicks > 0) {
                    this.DodgeAnimTicks -= 2;
                } else if (this.DodgeAnimTicks < 0) {
                    this.DodgeAnimTicks += 2;
                }

                if (this.AbilitiesTicksCooldown <= 0) {
                    this.bossAbilitiesHandle.tick();
                    if (this.getUnderlyingPlayer() == null) {
                        this.GlowStage = 0;
                    }
                } else {
                    this.AbilitiesTicksCooldown--;
                    if (this.getUnderlyingPlayer() == null) {
                        this.GlowStage = 1;
                    }
                }

                this.ActivatedAbility = this.getTarget() != null;
                if (this.SuperAbilitiesTicksCooldown > 0) {
                    this.SuperAbilitiesTicksCooldown--; //Super Abilities CoolDown
                }

                if (this.isAlive()) {
                    if (this.PassivesTicksCooldown <= 10) {
                        this.bossAbilitiesHandle.Passives(); //Passives
                    } else {
                        this.PassivesTicksCooldown -= 2;
                    }

                    if (this.isDashing()) {
                        DashingTicks--;
                        if (this.getTarget() == null) {
                            this.DashingTicks = 0;
                        }
                        for (int theta = 0; theta < 360; theta += 15) { // Ângulo horizontal
                            double angleTheta = Math.toRadians(theta);
                            for (int phi = 0; phi <= 180; phi += 15) { // Ângulo vertical
                                double anglePhi = Math.toRadians(phi);
                                double x = this.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * 4.0;
                                double y = this.getY() + Math.cos(anglePhi) * 4.0;
                                double z = this.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * 4.0;
                                Vec3 pos = new Vec3(x, y, z);
                                PlayerUtilProcedure.ParticlesUtil.sendParticles(
                                        this.getLevel(),
                                        ParticleTypes.GLOW,
                                        pos,
                                        0.3f, 0.2f, 0.3f,
                                        4, 0
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossBar.removePlayer(player);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ActivatedAbility")) {
            this.ActivatedAbility = tag.getBoolean("ActivatedAbility");
        }
        if (tag.contains("AbilitiesTicksCooldown")) {
            this.AbilitiesTicksCooldown = tag.getFloat("AbilitiesTicksCooldown");
        }
        if (tag.contains("PassivesTicksCooldown")) {
            this.PassivesTicksCooldown = tag.getInt("PassivesTicksCooldown");
        }
        if (tag.contains("SuperAbilitiesTicksCooldown")) {
            this.SuperAbilitiesTicksCooldown = tag.getInt("SuperAbilitiesTicksCooldown");
        }
        if (tag.contains("DodgeAnimTicks")) {
            this.DodgeAnimTicks = tag.getInt("DodgeAnimTicks");
        }
        if (tag.contains("DashingTicks")) {
            this.DashingTicks = tag.getInt("DashingTicks");
        }
        if (tag.contains("GlowStage")) {
            this.GlowStage = tag.getInt("GlowStage");
        }
        //if (tag.contains("DEVATTACKTESTTICK")) {
        //	this.DEVATTACKTESTTICK = tag.getInt("DEVATTACKTESTTICK");
        //}
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("ActivatedAbility", ActivatedAbility);
        tag.putFloat("AbilitiesTicksCooldown", AbilitiesTicksCooldown);
        tag.putInt("SuperAbilitiesTicksCooldown", SuperAbilitiesTicksCooldown);
        tag.putInt("PassivesTicksCooldown", PassivesTicksCooldown);
        tag.putInt("DodgeAnimTicks", DodgeAnimTicks);
        tag.putInt("DashingTicks", DodgeAnimTicks);
        tag.putInt("GlowStage", GlowStage);
        //tag.putInt("DEVATTACKTESTTICK", DEVATTACKTESTTICK);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(205f);
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(15f);
        Objects.requireNonNull(this.getAttribute(Attributes.ARMOR)).setBaseValue(10f);
        Objects.requireNonNull(this.getAttribute(Attributes.ARMOR_TOUGHNESS)).setBaseValue(2.5f);
        this.setHealth(205f);
        //this.setAbsorptionAmount(75f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    public boolean isDashing() {
        return this.DashingTicks > 0;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        this.AbilitiesTicksCooldown -= 5 + (0.05f * amount);

        if (source instanceof EntityDamageSource entityDamageSource && entityDamageSource.isThorns()) {
            return false;
        }

        if (source.isProjectile()) {
            return false;
        }

        if (source.isFire() || source.isExplosion()) {
            return super.hurt(source, amount * 0.01f);
        }

        Entity attacker = source.getDirectEntity();
        if (attacker == null) {
            attacker = source.getEntity();
        }

        if (attacker == null) {
            if (source == ChangedAddonDamageSources.SOLVENT) {
                return super.hurt(source, amount * 0.5f);
            }
            return super.hurt(source, amount * 0.25f);
        }

        if (source.getDirectEntity() == null) {
            super.hurt(source, amount);
        }

        if (attacker instanceof LivingEntity livingEntity
                && (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), livingEntity.getMainHandItem()) >= 1)) {
            return super.hurt(source, amount * 0.5f);
        } else {
            amount = amount / 6;
            if (amount > 2) {
                if (amount < 4) {
                    this.DodgeAnimTicks = this.getLevel().random.nextBoolean() ? DodgeAnimMaxTicks / 2 : -DodgeAnimMaxTicks / 2;
                }
                return super.hurt(source, amount);
            } else {
                if (attacker == this) {
                    this.DodgeAnimTicks = this.getLevel().random.nextBoolean() ? DodgeAnimMaxTicks : -DodgeAnimMaxTicks;
                    Vec3 pos = new Vec3(attacker.getX(), attacker.position().y + 1.5, attacker.position().z);
                    this.lookAt(EntityAnchorArgument.Anchor.EYES, pos);
                    return false;
                } else {
                    this.DodgeAnimTicks = this.getLevel().random.nextBoolean() ? DodgeAnimMaxTicks : -DodgeAnimMaxTicks;
                    Vec3 pos = new Vec3(attacker.getX(), attacker.position().y + 1.5, attacker.position().z);
                    this.lookAt(EntityAnchorArgument.Anchor.EYES, pos);
                    return false;
                }
            }
        }
    }

}
