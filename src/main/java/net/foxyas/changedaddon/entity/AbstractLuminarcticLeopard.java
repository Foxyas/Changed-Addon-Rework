package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.block.AbstractLuminarCrystal;
import net.foxyas.changedaddon.entity.CustomHandle.BossAbilitiesHandle;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.foxyas.changedaddon.registers.ChangedAddonDamageSources;
import net.foxyas.changedaddon.variants.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.EyeStyle;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public abstract class AbstractLuminarcticLeopard extends AbstractSnowLeopard {

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {
        @SubscribeEvent
        public static void WhenAttack(LivingAttackEvent event) {
            LivingEntity target = event.getEntityLiving();
            Entity source = event.getSource().getEntity();
            if (source instanceof AbstractLuminarcticLeopard lumi && lumi.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                PlayerUtilProcedure.ParticlesUtil.sendParticles(target.level, ParticleTypes.SNOWFLAKE, target.position(), 0.3f, 0.5f, 0.3f, 4, 0.25f);
                target.setTicksFrozen(target.getTicksFrozen() + (int) (target.getTicksRequiredToFreeze() * 0.25f));
                target.playSound(SoundEvents.PLAYER_HURT_FREEZE, 2f, 1f);
            } else if (source instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
                        && instance != null
                        && instance.getParent().is(ChangedAddonTransfurVariants.TransfurVariantTags.CAUSE_FREEZE_DMG)) {
                    PlayerUtilProcedure.ParticlesUtil.sendParticles(target.level, ParticleTypes.SNOWFLAKE, target.position(), 0.3f, 0.5f, 0.3f, 4, 0.25f);
                    target.setTicksFrozen(target.getTicksFrozen() + (int) (target.getTicksRequiredToFreeze() * 0.25f));
                    target.playSound(SoundEvents.PLAYER_HURT_FREEZE, 2f, 1f);
                }
            }
        }
    }

    public static <T extends AbstractLuminarcticLeopard> boolean canSpawnNear(EntityType<T> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        // Verifica se a luz do bloco é menor ou igual a 6
        if (world.getBrightness(LightLayer.BLOCK, pos) > 6) {
            return false;
        }

        // Certifica-se de que o bloco abaixo não é ar e é sólido
        BlockState blockBelow = world.getBlockState(pos.below());
        if (!blockBelow.isSolidRender(world, pos.below()) || !blockBelow.isFaceSturdy(world, pos.below(), Direction.UP)) {
            return false;
        }

        // Define uma área de checagem ao redor do spawn (raio de 6 blocos)
        AABB checkArea = new AABB(pos).inflate(6);

        // Verifica se há um Luminar Crystal Small (hearted) por perto
        boolean nearLuminarCrystal = world.getBlockStatesIfLoaded(checkArea)
                .anyMatch((state) -> state.is(ChangedAddonModBlocks.LUMINAR_CRYSTAL_SMALL.get()) &&
                        state.getValue(AbstractLuminarCrystal.CrystalSmall.HEARTED));

        if (!nearLuminarCrystal) {
            return false;
        }

        // **Novo: Checagem para limitar a quantidade de entidades**
        int maxEntitiesNear = 3; // Defina quantas entidades podem existir perto do cristal
        int currentEntities = world.getEntities(entityType, checkArea, entity -> true).size();

        return currentEntities < maxEntitiesNear; // Já tem muitas entidades perto, impedir novo spawn
        // Condições atendidas, pode spawnar
    }


    public final ServerBossEvent bossBar = new ServerBossEvent(
            this.getDisplayName(), // Nome exibido na boss bar
            BossEvent.BossBarColor.WHITE, // Cor da barra
            BossEvent.BossBarOverlay.NOTCHED_6 // Estilo da barra
    );

    @Override
    protected int getExperienceReward(Player player) {
            if (this.isBoss()){
                return super.getExperienceReward(player) * 50;
            }

        return super.getExperienceReward(player);
    }

    boolean ActivatedAbility = false;
    public float AbilitiesTicksCooldown = 20;
    public int SuperAbilitiesTicksCooldown = 0;
    public int PassivesTicksCooldown = 0;
    public int DashingTicks = 0;
    public final BossAbilitiesHandle bossAbilitiesHandle = new BossAbilitiesHandle(this);
    private static final EntityDataAccessor<Integer> DODGE_ANIM_TICKS =
            SynchedEntityData.defineId(AbstractLuminarcticLeopard.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DODGE_ANIM_TICKS, 0);
    }

    public int getDodgeAnimTicks() {
        return this.entityData.get(DODGE_ANIM_TICKS);
    }

    public void setDodgeAnimTicks(int ticks) {
        this.entityData.set(DODGE_ANIM_TICKS, ticks);
    }
    
    public final int DodgeAnimMaxTicks = 20;

    private boolean isBoss = false;

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
    public boolean canBeAffected(@NotNull MobEffectInstance mobEffectInstance) {
        return super.canBeAffected(mobEffectInstance);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.getUnderlyingPlayer() == null) {
            if (!this.level.isClientSide && this.isBoss()) {
                this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
            }

		/*if (this.DEVATTACKTESTTICK != 0){
			this.AbilitiesTicksCooldown = 0;
			this.ActivatedAbility = true;
		}*/

            if (!this.isNoAi()) {
                int ticks = this.getDodgeAnimTicks();
                if (ticks > 0) {
                    this.setDodgeAnimTicks(ticks - 2);
                } else if (ticks < 0) {
                    this.setDodgeAnimTicks(ticks + 2);
                }

                if (this.isBoss()) {
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
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.isBoss()) {
            this.bossBar.addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (this.isBoss()) {
            this.bossBar.removePlayer(player);
        }
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
            this.setDodgeAnimTicks(tag.getInt("DodgeAnimTicks"));
        }
        if (tag.contains("DashingTicks")) {
            this.DashingTicks = tag.getInt("DashingTicks");
        }
        if (tag.contains("GlowStage")) {
            this.GlowStage = tag.getInt("GlowStage");
        }
        if (tag.contains("isBoss")) {
            this.isBoss = tag.getBoolean("isBoss");
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
        tag.putInt("DodgeAnimTicks", this.getDodgeAnimTicks());
        tag.putInt("DashingTicks", DashingTicks);
        tag.putInt("GlowStage", GlowStage);
        tag.putBoolean("isBoss", this.isBoss);
        //tag.putInt("DEVATTACKTESTTICK", DEVATTACKTESTTICK);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        if (this.isBoss()) {
            handleBoss();
        }


        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    public void handleBoss(){
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(500f);
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(25f);
        Objects.requireNonNull(this.getAttribute(Attributes.ARMOR)).setBaseValue(10f);
        Objects.requireNonNull(this.getAttribute(Attributes.ARMOR_TOUGHNESS)).setBaseValue(2.5f);
        this.setHealth(500f);
        //this.setAbsorptionAmount(75f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
    }

    public boolean isDashing() {
        return this.DashingTicks > 0;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        this.AbilitiesTicksCooldown -= 5 + (0.05f * amount);

        // Ignora dano de espinhos
        if (source instanceof EntityDamageSource entityDamageSource && entityDamageSource.isThorns()) {
            return false;
        }

        // Imune a projéteis
        if (source.isProjectile() && this.isBoss()) {
            // Animação de esquiva e "ignorar" o dano
            this.setDodgeAnimTicks(getLevel().random.nextBoolean() ? DodgeAnimMaxTicks : -DodgeAnimMaxTicks);

            Entity attacker = source.getDirectEntity() != null ? source.getDirectEntity() : source.getEntity();
            if (attacker != null) {
                Vec3 lookPos = new Vec3(attacker.getX(), attacker.getY() + 1.5, attacker.getZ());
                this.lookAt(EntityAnchorArgument.Anchor.EYES, lookPos);
            }
            return false;
        } else if (source.isProjectile() && !this.isBoss()) {
            return super.hurt(source, amount * 1f);
        }

        // Dano de fogo ou explosão extremamente reduzido
        if (this.isBoss() && (source.isFire() || source.isExplosion())) {
            return super.hurt(source, amount * 0.01f);
        }

        // Obtém entidade causadora do dano (direta ou indireta)
        Entity attacker = source.getDirectEntity() != null ? source.getDirectEntity() : source.getEntity();

        // Dano sem atacante direto ou indireto
        if (attacker == null && this.isBoss()) {
            if (source == ChangedAddonDamageSources.SOLVENT) {
                return super.hurt(source, amount * 0.5f);
            }
            return super.hurt(source, amount * 0.25f);
        }

        // Se o atacante tiver o encantamento SOLVENT, recebe meio dano
        if (attacker instanceof LivingEntity livingEntity && this.isBoss()) {
            if (EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), livingEntity.getMainHandItem()) >= 1) {
                return super.hurt(source, amount * 0.5f);
            }

            // Caso contrário, reduz o dano e aplica lógica de esquiva
            float reducedAmount = amount / 6f;
            if (reducedAmount > 2f) {
                if (reducedAmount < 4f) {
                    this.setDodgeAnimTicks(getLevel().random.nextBoolean() ? DodgeAnimMaxTicks / 2 : -DodgeAnimMaxTicks / 2);
                }
                return super.hurt(source, reducedAmount);
            } else {
                // Animação de esquiva e "ignorar" o dano
                this.setDodgeAnimTicks(getLevel().random.nextBoolean() ? DodgeAnimMaxTicks : -DodgeAnimMaxTicks);

                Vec3 lookPos = new Vec3(attacker.getX(), attacker.getY() + 1.5, attacker.getZ());
                this.lookAt(EntityAnchorArgument.Anchor.EYES, lookPos);
                return false;
            }
        }

        // Caso nada acima se aplique, recebe dano normalmente
        return super.hurt(source, amount);
    }


}
