package net.foxyas.changedaddon.entity.defaults;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.ability.DodgeAbilityInstance;
import net.foxyas.changedaddon.block.AbstractLuminarCrystal;
import net.foxyas.changedaddon.entity.api.ICrawlAbleEntity;
import net.foxyas.changedaddon.entity.api.IHasBossMusic;
import net.foxyas.changedaddon.entity.customHandle.BossAbilitiesHandle;
import net.foxyas.changedaddon.init.*;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

public abstract class AbstractLuminarcticLeopard extends AbstractSnowLeopard implements ICrawlAbleEntity, IHasBossMusic {

    public static final int GLOW_NONE = 0;
    public static final int GLOW_PULSE = 1;
    public static final int GLOW_ALWAYS = 2;
    private static final EntityDataAccessor<Integer> GLOW_STAGE =
            SynchedEntityData.defineId(AbstractLuminarcticLeopard.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ACTIVATED_ABILITY =
            SynchedEntityData.defineId(AbstractLuminarcticLeopard.class, EntityDataSerializers.BOOLEAN);
    public final ServerBossEvent bossBar = new ServerBossEvent(
            this.getDisplayName(), // Nome exibido na boss bar
            BossEvent.BossBarColor.WHITE, // Cor da barra
            BossEvent.BossBarOverlay.NOTCHED_6 // Estilo da barra
    );
    public final BossAbilitiesHandle bossAbilitiesHandle = new BossAbilitiesHandle(this);
    //public final int DodgeAnimMaxTicks = 20;
    public float AbilitiesTicksCooldown = 20;
    public int SuperAbilitiesTicksCooldown = 0;
    public int PassivesTicksCooldown = 0;
    public int DashingTicks = 0;
    public DodgeAbilityInstance dodgeAbilityInstance = null;
    private boolean isBoss = false;
    private boolean Aggro = false;
    private boolean attributesApplied = false;

    //public int DEVATTACKTESTTICK = 0;
    public AbstractLuminarcticLeopard(EntityType<? extends AbstractSnowLeopard> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        this.setAttributes(this.getAttributes());
        this.dodgeAbilityInstance = this.registerAbility((this::canDodge), new DodgeAbilityInstance(ChangedAddonAbilities.DODGE.get(), IAbstractChangedEntity.forEntity(this)));
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
                .anyMatch((state) -> state.is(ChangedAddonBlocks.LUMINAR_CRYSTAL_SMALL.get()) &&
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

    public static LootTable.@NotNull Builder getLoot() {
        return LootTable.lootTable();
    }

    public boolean canDodge(DodgeAbilityInstance abilityInstance) {
        return true;
    }

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    public boolean isAggro() {
        return Aggro;
    }

    public void setAggro(boolean aggro) {
        Aggro = aggro;
    }

    @Override
    public int getExperienceReward() {
        if (this.isBoss()) {
            return super.getExperienceReward() * 50;
        }

        return super.getExperienceReward();
    }

    @Override
    protected boolean targetSelectorTest(LivingEntity livingEntity) {
        return this.isAggro() && !(livingEntity instanceof AbstractLuminarcticLeopard);
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GLOW_STAGE, 0);
        this.entityData.define(ACTIVATED_ABILITY, false);
    }

    public int getGlowStage() {
        return this.entityData.get(GLOW_STAGE);
    }

    public void setGlowStage(int stage) {
        this.entityData.set(GLOW_STAGE, stage);
    }

    public boolean isActivatedAbility() {
        return this.entityData.get(ACTIVATED_ABILITY);
    }

    public void setActivatedAbility(boolean value) {
        this.entityData.set(ACTIVATED_ABILITY, value);
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
    public void variantTick(Level level) {
        super.variantTick(level);

        if (this.isBoss() && !attributesApplied) {
            handleBoss();
        } else if (!this.isBoss() && attributesApplied) {
            handleNonBoss();
        }
    }

    @Override
    public @Nullable ResourceLocation getBossMusic() {
        return this.isBoss() ? ChangedAddonSoundEvents.LUMINARCTIC_LEOPARD.get().getLocation() : null;
    }

    @Override
    public LivingEntity getSelf() {
        return this;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.isBoss() && !attributesApplied) {
            handleBoss();
        }

        if (this.getUnderlyingPlayer() == null) {
            if (!this.isNoAi()) {
                if (this.dodgeAbilityInstance != null && this.dodgeAbilityInstance.isDodgeActive()) {
                    this.dodgeAbilityInstance.setDodgeActivate(false);
                }
                if (this.isBoss()) {
                    if (this.AbilitiesTicksCooldown <= 0) {
                        this.bossAbilitiesHandle.tick();
                        if (this.getUnderlyingPlayer() == null) {
                            this.setGlowStage(GLOW_NONE);
                        }
                    } else {
                        this.AbilitiesTicksCooldown--;
                        if (this.getUnderlyingPlayer() == null) {
                            this.setGlowStage(GLOW_PULSE);
                        }
                    }

                    this.setActivatedAbility(this.getTarget() != null);
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
                                    ParticlesUtil.sendParticles(
                                            this.level(),
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
            if (!this.level.isClientSide && this.isBoss()) {
                this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
            }
        }
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        if (this.isBoss()) {
            this.bossBar.addPlayer(player);
        }
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        if (this.isBoss()) {
            this.bossBar.removePlayer(player);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ActivatedAbility")) {
            this.setActivatedAbility(tag.getBoolean("ActivatedAbility"));
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
        if (tag.contains("DashingTicks")) {
            this.DashingTicks = tag.getInt("DashingTicks");
        }
        if (tag.contains("GlowStage")) {
            this.setGlowStage(tag.getInt("GlowStage"));
        }
        if (tag.contains("isBoss")) {
            this.isBoss = tag.getBoolean("isBoss");
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("ActivatedAbility", isActivatedAbility());
        tag.putFloat("AbilitiesTicksCooldown", AbilitiesTicksCooldown);
        tag.putInt("SuperAbilitiesTicksCooldown", SuperAbilitiesTicksCooldown);
        tag.putInt("PassivesTicksCooldown", PassivesTicksCooldown);
        tag.putInt("DashingTicks", DashingTicks);
        tag.putInt("GlowStage", this.getGlowStage());
        tag.putBoolean("isBoss", this.isBoss);
        //tag.putInt("DEVATTACKTESTTICK", DEVATTACKTESTTICK);
    }



    public void handleBoss() {
        attributesApplied = true;
        Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(500f);
        Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(17.5f);
        Objects.requireNonNull(this.getAttribute(Attributes.ARMOR)).setBaseValue(10f);
        Objects.requireNonNull(this.getAttribute(Attributes.ARMOR_TOUGHNESS)).setBaseValue(2.5f);
        this.setHealth(500f);
        //this.setAbsorptionAmount(75f);
        this.getBasicPlayerInfo().setEyeStyle(EyeStyle.TALL);
        IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    public void handleNonBoss() {
        attributesApplied = false;
        this.setAttributes(this.getAttributes());
        IAbstractChangedEntity.forEitherSafe(maybeGetUnderlying()).map(IAbstractChangedEntity::getTransfurVariantInstance).ifPresent(TransfurVariantInstance::refreshAttributes);
    }

    public boolean isDashing() {
        return this.DashingTicks > 0;
    }

    private void setDodging(Entity entity) {
        if (entity != null) {
            this.lookAt(EntityAnchorArgument.Anchor.FEET, entity.getEyePosition());
        }
        this.getNavigation().stop();
        if (this.dodgeAbilityInstance != null) {
            this.dodgeAbilityInstance.executeDodgeEffects(this, this.getTarget());
            this.dodgeAbilityInstance.setDodgeActivate(true);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        this.AbilitiesTicksCooldown -= (0.05f * amount);

        // Imune a projéteis
        if (source.getDirectEntity() instanceof ThrowableItemProjectile throwableItemProjectile) {
            if (this.isBoss()) {
                Entity attacker = throwableItemProjectile.getOwner() != null ? throwableItemProjectile.getOwner() : source.getEntity();
                setDodging(attacker);
            }
        }
        if (source.is(DamageTypeTags.IS_PROJECTILE) && source.getDirectEntity() instanceof AbstractArrow abstractArrow && abstractArrow.getPierceLevel() <= 0 && this.isBoss()) {
            // Animação de esquiva e "ignorar" o dano

            Entity attacker = source.getDirectEntity() != null ? source.getDirectEntity() : source.getEntity();
            setDodging(attacker);
            return false;
        } else if (source.is(DamageTypeTags.IS_PROJECTILE) && !this.isBoss()) {
            return super.hurt(source, amount);
        } else if (source.is(DamageTypeTags.IS_PROJECTILE) &&
                source.getDirectEntity() instanceof AbstractArrow abstractArrow &&
                abstractArrow.getPierceLevel() > 0 && this.isBoss()) {
            return super.hurt(source, amount);
        }

        // Dano de fogo ou explosão extremamente reduzido
        if (this.isBoss() && (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION))) {
            if (source.is(DamageTypeTags.IS_FIRE)) {
                return super.hurt(source, amount * 0.75f);
            }
            return super.hurt(source, amount * 0.25f);
        }

        // Obtém entidade causadora do dano (direta ou indireta)
        Entity attacker = source.getDirectEntity() != null ? source.getDirectEntity() : source.getEntity();

        // Dano sem atacante direto ou indireto
        if (attacker == null && this.isBoss()) {
            if (source.is(ChangedAddonDamageSources.LATEX_SOLVENT.key())) {
                return super.hurt(source, amount * 1.25f);
            } else if (source.getMsgId().contains("latex_solvent")) {
                return super.hurt(source, amount * 1.25f);
            }
            return super.hurt(source, amount * 0.25f);
        }

        if (attacker instanceof LivingEntity livingEntity && this.isBoss()) {
            if (livingEntity.getAttribute(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get()) != null && livingEntity.getAttributeValue(ChangedAddonAttributes.LATEX_SOLVENT_DAMAGE_MULTIPLIER.get()) >= 1) {
                return super.hurt(source, amount * 1.25f);
            } else if (source.getMsgId().contains("latex_solvent")) {
                return super.hurt(source, amount * 1.25f);
            }

            // Caso contrário, reduz o dano e aplica lógica de esquiva
            float reducedAmount = amount / 6f;
            if (reducedAmount > 2f) {
                if (reducedAmount < 4f) {
                    setDodging(attacker);
                }
                return super.hurt(source, reducedAmount);
            } else {
                // Animação de esquiva e "ignorar" o dano

                setDodging(attacker);
                return false;
            }
        }

        // Caso nada acima se aplique, recebe dano normalmente
        return super.hurt(source, amount);
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {

        @SubscribeEvent
        public static void OnLuminarcticSpawn(MobSpawnEvent.FinalizeSpawn spawnEvent) {
            CompoundTag spawnTag = spawnEvent.getSpawnTag();
            Mob entity = spawnEvent.getEntity();
            if (!(entity instanceof AbstractLuminarcticLeopard abstractLuminarcticLeopard)) return;
            boolean attributesApplied = abstractLuminarcticLeopard.attributesApplied;

            if (spawnTag != null && spawnTag.contains("isBoss") && spawnTag.getBoolean("isBoss")) {
                if (!attributesApplied) {
                    abstractLuminarcticLeopard.handleBoss();
                }
            } else if (abstractLuminarcticLeopard.isBoss()) {
                if (!attributesApplied) {
                    abstractLuminarcticLeopard.handleBoss();
                }
            }
        }

        @SubscribeEvent
        public static void WhenAttack(LivingHurtEvent event) {
            LivingEntity target = event.getEntity();
            Entity source = event.getSource().getEntity();

            if (event.getSource().is(DamageTypes.THORNS)) {
                return;
            }

            int pTicksFrozen = target.getTicksFrozen() + (int) (target.getTicksRequiredToFreeze() * 0.25f);
            if (source instanceof AbstractLuminarcticLeopard lumi && lumi.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
                ParticlesUtil.sendParticles(target.level, ParticleTypes.SNOWFLAKE, target.getEyePosition(), 0.3f, 0.5f, 0.3f, 4, 0.05f);
                target.setTicksFrozen(Math.min(target.getTicksRequiredToFreeze(), pTicksFrozen));
                target.playSound(SoundEvents.PLAYER_HURT_FREEZE, 2f, 1f);
            } else if (source instanceof Player player) {
                TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(player);
                if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()
                        && instance != null
                        && instance.getParent().is(ChangedAddonTags.TransfurTypes.CAUSE_FREEZING)) {
                    ParticlesUtil.sendParticles(target.level, ParticleTypes.SNOWFLAKE, target.getEyePosition(), 0.3f, 0.5f, 0.3f, 4, 0.05f);
                    target.setTicksFrozen(Math.min(target.getTicksRequiredToFreeze(), pTicksFrozen));
                    target.playSound(SoundEvents.PLAYER_HURT_FREEZE, 2f, 1f);
                }
            }
        }

        @SubscribeEvent
        public static void onEntityDrop(LivingDropsEvent event) {
            LivingEntity entity = event.getEntity();
            Level level = entity.level;

            // Verifica se é um mob específico, por exemplo, um Luminarctic Leopard
            if (entity instanceof AbstractLuminarcticLeopard leopard) {
                // Verifica se tem a NBT isBoss = 1b
                if (leopard.isBoss()) {
                    // Cria o item que será dropado
                    ItemStack item = new ItemStack(ChangedAddonItems.LUMINAR_CRYSTAL_SHARD_HEARTED.get());

                    // Cria o drop
                    ItemEntity drop = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), item);

                    // Adiciona à lista de drops
                    event.getDrops().add(drop);
                }
            }
        }
    }
}
