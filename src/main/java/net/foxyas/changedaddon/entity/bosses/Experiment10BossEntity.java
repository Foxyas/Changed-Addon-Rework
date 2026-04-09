package net.foxyas.changedaddon.entity.bosses;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.ai.goals.exp10.ClawsComboAttackGoal;
import net.foxyas.changedaddon.entity.ai.goals.exp10.ThrowWitherProjectileGoal;
import net.foxyas.changedaddon.entity.ai.goals.exp10.WitherWave;
import net.foxyas.changedaddon.entity.ai.goals.generic.BreakBlocksAroundGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.BurstAttack;
import net.foxyas.changedaddon.entity.ai.goals.generic.LatexPullEntityGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.DashPunchGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.LeapSmashGoal;
import net.foxyas.changedaddon.entity.ai.goals.generic.attacks.SimpleAntiFlyingAttack;
import net.foxyas.changedaddon.entity.api.IAlphaAbleEntity;
import net.foxyas.changedaddon.init.ChangedAddonCriteriaTriggers;
import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.foxyas.changedaddon.init.ChangedAddonGameRules;
import net.foxyas.changedaddon.init.ChangedAddonSoundEvents;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.variant.ChangedAddonTransfurVariants;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.entity.variant.TransfurVariantInstance;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Experiment10BossEntity extends Experiment10Entity implements IExp10Logic {

    private static final EntityDataAccessor<Boolean> PHASE2 =
            SynchedEntityData.defineId(Experiment10BossEntity.class, EntityDataSerializers.BOOLEAN);
    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.RED, ServerBossEvent.BossBarOverlay.NOTCHED_6);

    public Experiment10BossEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.EXPERIMENT_10_BOSS.get(), world);
    }

    public Experiment10BossEntity(EntityType<Experiment10BossEntity> type, Level world) {
        super(type, world);
        this.setAttributes(getAttributes());
        xpReward = 3000;
        setNoAi(false);
        setPersistenceRequired();
        applyDefaultBasicPlayerInfo();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = ChangedEntity.createLatexAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 300);
        builder = builder.add(Attributes.ARMOR, 20);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.25);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 1);
        return builder;
    }

    private static GearTier getGearTier(LivingEntity entity) {

        double armor = entity.getAttributeValue(Attributes.ARMOR);
        double toughness = entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS);

        double score = armor + (toughness * 2);

        if (score >= 40) return GearTier.HIGH;
        if (score >= 15) return GearTier.MID;
        return GearTier.LOW;
    }

    @Override
    public void variantTick(Level level) {
        super.variantTick(level);
        if (getUnderlyingPlayer() == null) return;

        Player playerInControl = this.getUnderlyingPlayer();
        TransfurVariantInstance<?> instance = ProcessTransfur.getPlayerTransfurVariant(playerInControl);
        if (instance == null) return;

        if (playerInControl.level().getLevelData().getGameRules().getBoolean(ChangedAddonGameRules.NEED_PERMISSION_FOR_BOSS_TRANSFUR)) {
            if (!ChangedAddonVariables.ofOrDefault(playerInControl).Exp10TransfurAllowed) {
                ProcessTransfur.setPlayerTransfurVariant(playerInControl, ChangedAddonTransfurVariants.EXPERIMENT_10.get(), TransfurContext.hazard(TransfurCause.GRAB_ABSORB), 1, false);
            }
        }
    }

    @Override
    public @Nullable ResourceLocation getBossMusic() {
        return ChangedAddonSoundEvents.EXP10_THEME.get().getLocation();
    }

    @Override
    public LivingEntity getSelf() {
        return this;
    }

    @Override
    public float getMusicVolume() {
        return 0.5f;
    }

    protected void setAttributes(AttributeMap attributes) {
        super.setAttributes(attributes);

        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((325));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.17);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.1);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(12);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(10);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(6);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0.25);
        attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(0.8);
        attributes.getInstance(ChangedAttributes.JUMP_STRENGTH.get()).setBaseValue(1.5f);
        attributes.getInstance(ChangedAttributes.FALL_RESISTANCE.get()).setBaseValue(2.5F);
    }

    @Override
    public boolean startRiding(@NotNull Entity EntityIn, boolean force) {
        if (EntityIn instanceof Boat || EntityIn instanceof Minecart) {
            return false;
        }

        return super.startRiding(EntityIn, force);
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity target) {
        if (target.getEyeY() > this.getEyeY() + 1) {
            return super.getMeleeAttackRangeSqr(target) * 1.5D;
        }

        return super.getMeleeAttackRangeSqr(target);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(5, new ClawsComboAttackGoal(this, //PathfinderMob -> holder,
                UniformInt.of(150, 200), //IntProvider -> cooldown,
                UniformInt.of(3, 6), //IntProvider -> attackCount,
                UniformInt.of(20, 40), //IntProvider -> castDuration,
                UniformFloat.of(6, 8))); //FloatProvider -> damage)

        this.goalSelector.addGoal(6, new BurstAttack(this));
        this.goalSelector.addGoal(6, new WitherWave(this, UniformInt.of(60, 120)));
        this.goalSelector.addGoal(20, new SimpleAntiFlyingAttack(this,
                UniformInt.of(60, 100),
                3,
                32,
                8f,
                10));
        this.goalSelector.addGoal(10, new LeapSmashGoal(this));
        this.goalSelector.addGoal(15, new DashPunchGoal(this));
        this.goalSelector.addGoal(10, new BreakBlocksAroundGoal(this));
        this.goalSelector.addGoal(10, new ThrowWitherProjectileGoal(this, UniformInt.of(60, 120), UniformInt.of(1, 8), 36));
        this.goalSelector.addGoal(10, new LatexPullEntityGoal(this, 32, 1));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        String id = source.getMsgId();

        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;

        switch (id) {
            case "fall", "cactus", "drown", "lightningBolt", "anvil", "dragonBreath", "wither", "witherSkull" -> {
                return false;
            }
            case "trident" -> {
                maybeSendReactionToPlayer(source);
                return super.hurt(source, amount * 0.5f);
            }
        }

        if (source.is(DamageTypeTags.IS_FIRE)) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0f);
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            maybeSendReactionToPlayer(source);
            return super.hurt(source, amount * 0.5f);
        }

        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource pDamageSource, float pDamageAmount) {
        super.actuallyHurt(pDamageSource, pDamageAmount);

        float currentHealth = this.getHealth();
        float maxHealth = this.getMaxHealth();

        if (this.getUnderlyingPlayer() == null && currentHealth <= maxHealth * 0.75f && !isPhase2()) {
            this.setPhase2(true);
            level.playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.HOSTILE, 1, 0);
        }
    }

    private void maybeSendReactionToPlayer(DamageSource source) {
        if (!(source.getEntity() instanceof Player player)) return;

        if (this.random.nextFloat() <= 0.25f) {
            if (source.is(DamageTypeTags.IS_PROJECTILE)) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp10.reaction.range_attacks"), true);
            } else if (source.is(DamageTypeTags.IS_FIRE)) {
                player.displayClientMessage(Component.translatable("entity_dialogues.changed_addon.exp10.reaction.fire_damage"), true);
            }
        }
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance mobEffectInstance) {
        if (mobEffectInstance.getEffect() == MobEffects.WITHER) {
            return false;
        }

        return super.canBeAffected(mobEffectInstance);
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();

        float maxHealth = this.getMaxHealth();
        float currentHealth = this.getHealth();
        float healthRatio = currentHealth / maxHealth;

        // Se estiver com menos de 50% da vida, simula que 50% é o "cheio" da barra
        if (healthRatio <= 0.75f) {
            this.bossInfo.setProgress(healthRatio / 0.75f);
            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_10) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_10);
            }
            this.bossInfo.setName(bossInfo.getName().copy().withStyle(style -> style.withColor(ChatFormatting.DARK_RED)));
        } else {
            this.bossInfo.setProgress(healthRatio);
            if (this.bossInfo.getOverlay() != BossEvent.BossBarOverlay.NOTCHED_6) {
                this.bossInfo.setOverlay(BossEvent.BossBarOverlay.NOTCHED_6);
            }
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public Gender getGender() {
        return Gender.FEMALE;
    }

    @Override
    protected EntityDataAccessor<Boolean> getPhase2DataAccessor() {
        return PHASE2;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (firstTick) {
            applyDefaultBasicPlayerInfo();
        }

        SetDefense(this);
        SetAttack(this);
        SetSpeed(this);
        this.crawlingSystem((float) this.getAttributeValue(ForgeMod.SWIM_SPEED.get()) * 0.35f);
    }

    public void SetDefense(Experiment10BossEntity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "ArmorChange", 20, AttributeModifier.Operation.ADDITION);
        AttributeModifier AttibuteDefenseChange = new AttributeModifier(UUID.fromString("10-10-0-0-0"), "ArmorChange", 0.7, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.isPhase2()) {
            if (!((entity.getAttribute(Attributes.ARMOR).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.ARMOR).addTransientModifier(AttibuteChange);
            }

            if (!((entity.getAttribute(Attributes.ARMOR_TOUGHNESS).hasModifier(AttibuteDefenseChange)))) {
                entity.getAttribute(Attributes.ARMOR_TOUGHNESS).addTransientModifier(AttibuteDefenseChange);
            }

        } else {
            entity.getAttribute(Attributes.ARMOR).removeModifier(AttibuteChange);
            entity.getAttribute(Attributes.ARMOR_TOUGHNESS).removeModifier(AttibuteDefenseChange);
        }
    }

    public void SetAttack(Experiment10BossEntity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Attack", 0.6667, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.isPhase2()) {
            if (!((entity.getAttribute(Attributes.ATTACK_DAMAGE).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.ATTACK_DAMAGE).addTransientModifier(AttibuteChange);
            }
        } else {
            entity.getAttribute(Attributes.ATTACK_DAMAGE).removeModifier(AttibuteChange);
        }
    }

    public void SetSpeed(Experiment10BossEntity entity) {
        AttributeModifier AttibuteChange = new AttributeModifier(UUID.fromString("10-0-0-0-0"), "Speed", -0.4, AttributeModifier.Operation.MULTIPLY_BASE);
        if (entity.getPose() == Pose.SWIMMING) {
            if (!((entity.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(AttibuteChange)))) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(AttibuteChange);
            }
        } else {
            entity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(AttibuteChange);
        }
    }

    @Override
    public void WhenPattedReaction(Player player, InteractionHand hand) {
        if (!(player.level() instanceof ServerLevel)) return;
        if (player instanceof ServerPlayer serverPlayer) {
            ChangedAddonCriteriaTriggers.PAT_ENTITY_TRIGGER.Trigger(serverPlayer, this, "pats_on_the_beast");
        }

        List<Component> translatableComponentList = new ArrayList<>();
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_0"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_1"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_2"));
        translatableComponentList.add(Component.translatable("entity_dialogues.changed_addon.exp10.pat.type_3"));

        player.level().addParticle(
                ChangedParticles.emote(this, Emote.ANGRY),
                this.getX(),
                this.getY() + (double) this.getDimensions(this.getPose()).height + 0.65,
                this.getZ(),
                0.0f,
                0.0f,
                0.0f
        );

        Component translatableComponent = translatableComponentList.get(this.getRandom().nextInt(translatableComponentList.size()));
        MutableComponent entityChat = Component.translatable("chat.type.text", this.getDisplayName(), translatableComponent);

        this.playSound(SoundEvents.WITHER_AMBIENT, 0.05f, 2f);
        player.displayClientMessage(entityChat, false);
        applyRampage();
    }

    private void applyRampage() {
        MobEffectInstance thisEffect = this.getEffect(MobEffects.DAMAGE_BOOST);
        MobEffectInstance mobEffectInstance;
        if (thisEffect != null) {
            int pDuration = thisEffect.getDuration() + 10;
            int pAmplifier = Mth.clamp(thisEffect.getAmplifier() + 1, 0, 5);
            mobEffectInstance = new MobEffectInstance(MobEffects.DAMAGE_BOOST, pDuration, pAmplifier, thisEffect.isAmbient(), thisEffect.isVisible(), thisEffect.showIcon());
        } else {
            mobEffectInstance = new MobEffectInstance(MobEffects.DAMAGE_BOOST, 20, 0, true, true, true);
        }
        this.addEffect(mobEffectInstance);
    }

    @Override
    public boolean canCauseGrabDamage() {
        return true;
    }

    @Override
    public void applyAlphaAttributesModifiers(LivingEntity entity, float normalized) {
        IAlphaAbleEntity.apply(entity, Attributes.MAX_HEALTH, IAlphaAbleEntity.MAX_HEALTH, "Alpha Max Health", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_DAMAGE, IAlphaAbleEntity.ATTACK_DAMAGE, "Alpha Attack Damage", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ARMOR, IAlphaAbleEntity.ARMOR, "Alpha Armor", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ARMOR_TOUGHNESS, IAlphaAbleEntity.ARMOR_TOUGHNESS, "Alpha Armor Toughness", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.STEP_HEIGHT_ADDITION.get(), IAlphaAbleEntity.STEP_HEIGHT, "Alpha Step Height", normalized, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ChangedAttributes.TRANSFUR_DAMAGE.get(), IAlphaAbleEntity.TRANSFUR_DAMAGE, "Alpha Transfur Damage", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_KNOCKBACK, IAlphaAbleEntity.ATTACK_KNOCKBACK, "Alpha Knockback", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, Attributes.ATTACK_SPEED, IAlphaAbleEntity.ATTACK_SPEED, "Alpha Attack Speed", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.ENTITY_REACH.get(), IAlphaAbleEntity.ENTITY_REACH, "Alpha Attack Reach", normalized * 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ForgeMod.BLOCK_REACH.get(), IAlphaAbleEntity.BLOCK_REACH, "Alpha Block Reach", normalized * 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);

        IAlphaAbleEntity.apply(entity, ChangedAttributes.JUMP_STRENGTH.get(), IAlphaAbleEntity.JUMP_STRENGTH, "Alpha Jump Strength", normalized * 0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    private enum GearTier {
        LOW,
        MID,
        HIGH
    }

    @Mod.EventBusSubscriber(modid = ChangedAddonMod.MODID)
    public static class WhenAttackAEntity {

        @SubscribeEvent
        public static void onBossDamagePlayer(LivingHurtEvent event) {
            if (!(event.getSource().getEntity() instanceof Experiment10BossEntity)) return;
            if (!(event.getEntity() instanceof Player target)) return;

            GearTier tier = getGearTier(target);

            switch (tier) {
                case LOW, MID -> event.setAmount(event.getAmount());
                case HIGH -> event.setAmount(event.getAmount() * 1.25F);
            }
        }

        @SubscribeEvent
        public static void onPlayerDamageBoss(LivingHurtEvent event) {
            if (!(event.getSource().getEntity() instanceof Player source)) return;
            if (!(event.getEntity() instanceof Experiment10BossEntity)) return;

            GearTier tier = getGearTier(source);

            switch (tier) {
                case LOW -> event.setAmount(event.getAmount() * 2F);
                case MID, HIGH -> event.setAmount(event.getAmount());
            }
        }
    }
}
