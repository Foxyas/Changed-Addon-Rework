
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.entity.CustomHandle.AttributesHandle;
import net.foxyas.changedaddon.init.ChangedAddonModEnchantments;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.procedures.PlayerUtilProcedure;
import net.ltxprogrammer.changed.Changed;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.EyeStyle;
import net.ltxprogrammer.changed.entity.Gender;
import net.ltxprogrammer.changed.entity.TransfurMode;
import net.ltxprogrammer.changed.entity.beast.AbstractSnowLeopard;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.init.ChangedSounds;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.ltxprogrammer.changed.util.CameraUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;
import net.minecraft.network.chat.TextComponent;

public class LuminarcticLeopardEntity extends AbstractSnowLeopard {

	private static final SoundEvent TestMusic = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(ChangedAddonMod.MODID, "experiment009_theme_phase2"));

	private final ServerBossEvent bossBar = new ServerBossEvent(
			this.getDisplayName(), // Nome exibido na boss bar
			BossEvent.BossBarColor.WHITE, // Cor da barra
			BossEvent.BossBarOverlay.NOTCHED_6 // Estilo da barra
	);

	private boolean ActivatedAbility = false;
	public float AbilitiesTicksCooldown = 20;
	public int SuperAbilitiesTicksCooldown = 0;
	public int PassivesTicksCooldown = 0;
	public boolean isDashing = false;
	private final BossAbilitiesHandle bossAbilitiesHandle = new BossAbilitiesHandle(this);
	public int DodgeAnimTicks = 0;
	public final int DodgeAnimMaxTicks = 20;

	//public int DEVATTACKTESTTICK = 0;

	public LuminarcticLeopardEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.LUMINARCTIC_LEOPARD.get(), world);
	}

	public LuminarcticLeopardEntity(EntityType<LuminarcticLeopardEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 100;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
		setPersistenceRequired();
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
		if (tag.contains("ActivatedAbility")){
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
		if (tag.contains("isDashing")) {
			this.isDashing = tag.getBoolean("isDashing");
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
		tag.putBoolean("isDashing", isDashing);
		tag.putInt("DodgeAnimTicks", DodgeAnimTicks);
		//tag.putInt("DEVATTACKTESTTICK", DEVATTACKTESTTICK);
	}

	protected void setAttributes(AttributeMap attributes) {
		//Attack stats
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((6));
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(6.0f);
		attributes.getInstance(Attributes.ATTACK_KNOCKBACK).setBaseValue(
				AttributesHandle.DefaultPlayerAttributes().getBaseValue(Attributes.ATTACK_KNOCKBACK) + 1.5f
		);

		//Armor Stats
		attributes.getInstance(Attributes.ARMOR).setBaseValue(8);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(2);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);

		//Health Stats
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((60));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(128.0F);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.25f);
		attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.05f);
	}

	@Override
	public TransfurMode getTransfurMode() {
		if (this.getTarget() != null && (this.getTarget().getHealth() / this.getTarget().getMaxHealth() * 100) <= 15) {
			return TransfurMode.ABSORPTION;
		}
		return TransfurMode.NONE;
	}

	@Override
	protected boolean targetSelectorTest(LivingEntity livingEntity) {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	@Override
	public boolean removeWhenFarAway(double distanceToClosestPlayer) {
		return false;
	}

	@Override
	public double getMyRidingOffset() {
		return -0.35D;
	}

	public double getTorsoYOffset(ChangedEntity self) {
		float ageAdjusted = (float)self.tickCount * 0.33333334F * 0.25F * 0.15F;
		float ageSin = Mth.sin(ageAdjusted * 3.1415927F * 0.5F);
		float ageCos = Mth.cos(ageAdjusted * 3.1415927F * 0.5F);
		float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
		return (double)(Mth.lerp(Mth.lerp(1.0F - Mth.abs(Mth.positiveModulo(ageAdjusted, 2.0F) - 1.0F), ageSin * ageSin * ageSin * ageSin, 1.0F - ageCos * ageCos * ageCos * ageCos), 0.95F, 0.87F) + bpiSize);
	}

	public double getTorsoYOffsetForFallFly(ChangedEntity self){
		float bpiSize = (self.getBasicPlayerInfo().getSize() - 1.0F) * 2.0F;
		return 0.375 + bpiSize;
	}

	@Override
	public double getPassengersRidingOffset() {
		if (this.getPose() == Pose.STANDING || this.getPose() == Pose.CROUCHING) {
			return super.getPassengersRidingOffset() + this.getTorsoYOffset(this) + (this.isCrouching() ? 1.2 : 1.15);
		}
		return getTorsoYOffsetForFallFly(this);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
	}

	public static void init() {
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 6);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 1.25f);
		builder = builder.add(Attributes.MAX_HEALTH, 60F);
		builder = builder.add(Attributes.ARMOR, 8F);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 8);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}

	@Override
	public Gender getGender() {
		return Gender.MALE;
	}

	public boolean isActivatedAbility() {
		return ActivatedAbility;
	}

	public void SetActivatedAbility(boolean value){
		this.ActivatedAbility = value;
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (!this.level.isClientSide) {
			this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
		}

		/*if (this.DEVATTACKTESTTICK != 0){
			this.AbilitiesTicksCooldown = 0;
			this.ActivatedAbility = true;
		}*/

		if (this.DodgeAnimTicks > 0){
			this.DodgeAnimTicks -= 2;
		} else if (this.DodgeAnimTicks < 0){
			this.DodgeAnimTicks += 2;
		}

		if (this.AbilitiesTicksCooldown <= 0){
			this.bossAbilitiesHandle.tick();
		} else {
			this.AbilitiesTicksCooldown --;
		}

		this.ActivatedAbility = this.getTarget() != null;
		if (this.SuperAbilitiesTicksCooldown > 0){
			this.SuperAbilitiesTicksCooldown --; //Super Abilities CoolDown
		}

		if (this.isAlive()){

			if (this.PassivesTicksCooldown <= 10){
				this.bossAbilitiesHandle.Passives(); //Passives
			} else {
				this.PassivesTicksCooldown -= 2;
			}

			if (isDashing){
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

	@Override
	public boolean hurt(@NotNull DamageSource source, float amount) {
		this.AbilitiesTicksCooldown -= 5 + (0.05f * amount);

		if (source instanceof EntityDamageSource entityDamageSource && entityDamageSource.isThorns()){
			return false;
		}

		if (source.isProjectile()){
			return false;
		}

		if (source.isFire() || source.isExplosion()){
			return super.hurt(source, amount * 0.01f);
		}

		Entity attacker = source.getDirectEntity();
		if (attacker == null){ attacker = source.getEntity(); }

		if (attacker == null){
			return super.hurt(source, amount);
		}

		if (source.getDirectEntity() == null){
			super.hurt(source, amount);
		}

		if (attacker instanceof LivingEntity livingEntity && EnchantmentHelper.getItemEnchantmentLevel(ChangedAddonModEnchantments.SOLVENT.get(), livingEntity.getMainHandItem()) >= 5) {
			return super.hurt(source, amount * 0.5f);
		} else {
			amount = amount / 6;
			if (amount > 2){
				this.DodgeAnimTicks = this.getLevel().random.nextBoolean() ? DodgeAnimMaxTicks / 2 : -DodgeAnimMaxTicks / 2;
				return super.hurt(source, amount);
			} else if (attacker == this){
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

	// Class for handling boss abilities
	private static class BossAbilitiesHandle {
		private final LuminarcticLeopardEntity boss;

		public BossAbilitiesHandle(LuminarcticLeopardEntity boss) {
			this.boss = boss;
		}

		public void tick() {
			// Seleciona aleatoriamente uma habilidade para ser executada
			Random random = boss.getLevel().random;
			int abilityIndex = random.nextInt(8); // 8 habilidades ativas restantes, índices de 0 a
			LivingEntity bossTarget = this.boss.getTarget();

			/*if (boss.DEVATTACKTESTTICK != 0){
				int abilitytest = boss.DEVATTACKTESTTICK;
				switch (abilitytest) {
					case 1:
						glacialSpikes();
						break;
					case 2:
						frostNova();
						break;
					case 3:
						crystallineShield();
						break;
					case 4:
						radioactiveBurst();
						break;
					case 5:
						irradiatedPulse();
						break;
					case 6:
						radiantField();
						break;
					case 7:
						meltdown();
						break;
					case 8:
						arcticDash();
						break;
				}
			}*/


			if (bossTarget != null){
				if (bossTarget.distanceTo(boss) >= 4){
					this.boss.isDashing = true;
					arcticDash();
				} else if (bossTarget.distanceTo(boss) <= 4) {
					// Habilidades ativas
					switch (abilityIndex) {
						case 0:
							glacialSpikes();
							break;
						case 1:
							frostNova();
							break;
						case 2:
							crystallineShield();
							break;
						case 3:
							radioactiveBurst();
							break;
						case 4:
							irradiatedPulse();
							break;
						case 5:
							radiantField();
							break;
						case 6:
							meltdown();
							break;
						case 7:
							arcticDash();
							break;
					}
				}
			}
		}

		// Arctic Dash Ability
		public void arcticDash() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0 && boss.isOnGround()) { // Executa quando o cooldown for 0
				// Primeiro Dash: Direção do olhar do boss
				Vec3 lookDirection = boss.getLookAngle().normalize();
				boss.setDeltaMovement(lookDirection.scale(2.5)); // Dash rápido na direção do olhar
				// Segundo Dash: Direção do boss para o alvo
				LivingEntity target = boss.getTarget();
				if (target != null) {
					Vec3 targetDirection = target.position().subtract(boss.position()).normalize();
					boss.setDeltaMovement(targetDirection.scale(2.5)); // Dash rápido para a posição do alvo
					// Para cada entidade dentro da área do dash
					for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(3))) {
						if (entity != boss) {
							entity.hurt(DamageSource.mobAttack(boss).bypassInvul().bypassArmor(), 4.0F);
							entity.knockback(1.5, boss.getX() - entity.getX(), boss.getZ() - entity.getZ());

							// Cria partículas de glow no local da entidade atingida
							Vec3 pos = new Vec3(entity.position().x,entity.position().y + 0.5,entity.position().z);
							PlayerUtilProcedure.ParticlesUtil.sendParticles(entity.level,ParticleTypes.GLOW,pos,0.3f,0.2f,0.3f,25,1);
							entity.playSound(SoundEvents.BEACON_AMBIENT, 4.5f, 0);
						}
					}
					boss.playSound(ChangedSounds.BOW2, 4.5f, 1);
					boss.AbilitiesTicksCooldown = 60; // Set cooldown for the next activation
				}
				this.boss.isDashing = false;
			}
		}



		// Glacial Spikes Ability
		public void glacialSpikes() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
				for (int i = 0; i < 16; i++) {
						double angle = Math.toRadians(i * 45); // 8 direções
						double x = boss.getX() + Mth.cos((float) angle) * 3.0;
						double z = boss.getZ() + Mth.sin((float) angle) * 3.0;
						Vec3 pos = new Vec3(x,boss.getY(),z);
						PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.getLevel(), ParticleTypes.SNOWFLAKE, pos, 0.3f, 0.2f, 0.3f, 15,0);
						for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(3.0))) {
							if (entity != boss) {
								entity.hurt(DamageSource.mobAttack(boss).bypassMagic(), 8.0F);
								entity.knockback(1.0, boss.getX() - entity.getX(), boss.getZ() - entity.getZ());
							}
						}
				}
				boss.playSound(SoundEvents.GENERIC_EXPLODE,4.5f,1);
				boss.AbilitiesTicksCooldown = 100; // Set cooldown for the next activation
			}
		}

		// Frost Nova Ability
		public void frostNova() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
				PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.getLevel(), ParticleTypes.CLOUD, boss.position(), 0.3f, 0.2f, 0.3f, 15,0);
				for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(6.0))) {
					if (entity != boss) {
						entity.hurt(DamageSource.mobAttack(boss).bypassArmor(), 12.0f);
						if (!entity.getLevel().isClientSide()) {
							entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 2));
						}
					}
				}
				boss.playSound(SoundEvents.GENERIC_EXPLODE, 4.5f,1);
				boss.AbilitiesTicksCooldown = 150; // Set cooldown for the next activation
			}
		}

		// Crystalline Shield Ability
		public void crystallineShield() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
				for (int i = 0; i < 16; i++) {
					double angle = Math.toRadians(i * 45); // 8 direções
					double x = boss.getX() + Mth.cos((float) angle) * 3.0;
					double z = boss.getZ() + Mth.sin((float) angle) * 3.0;
					Vec3 pos = new Vec3(x,boss.getY(),z);
					PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.getLevel(), ParticleTypes.END_ROD, pos, 0.3f, 0.2f, 0.3f, 15,0);
				}
				boss.playSound(SoundEvents.BEACON_AMBIENT, 4.5f,0);
				boss.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2)); // Resistência por 5 segundos
				boss.AbilitiesTicksCooldown = 80; // Set cooldown for the next activation
			}
		}

		// Radioactive Burst Ability
		public void radioactiveBurst() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
				for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(5.0))) {
					if (entity != boss) {
						entity.hurt(DamageSource.mobAttack(boss).bypassMagic().bypassArmor(), 9.0F);
						if (!entity.getLevel().isClientSide()){
							entity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
						}
					}
				}
				boss.playSound(SoundEvents.GENERIC_EXPLODE, 4.5f,2);
				PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.getLevel(), ParticleTypes.SMOKE, this.boss.position(), 0.1f, 0.2f, 0.1f, 15,0);
				boss.AbilitiesTicksCooldown = 80; // Set cooldown for the next activation
			}
		}

		// Toxic Glare Ability
		public void toxicGlare(LivingEntity entity) {
			if (entity == null || boss == null){
				return;
			}

			if (entity instanceof Player player) {
				// Calcula se o jogador está olhando para o boss
				Vec3 lookVec = player.getLookAngle().normalize(); // Vetor de direção do olhar do jogador
				Vec3 directionToBoss = boss.position().subtract(player.position()).normalize(); // Vetor direção do jogador para o boss
				double dotProduct = lookVec.dot(directionToBoss); // Produto escalar para verificar alinhamento
				player.displayClientMessage(new TextComponent("Dot Value = " + dotProduct), true); //Debug
				// Se o ângulo entre o olhar e o boss for menor que 30 graus
				if (dotProduct > 0.95) { // ~30 graus de tolerância (cos(30º) ≈ 0.866)
					// Verifica se a visão está limpa
					Level world = player.getLevel();
					Vec3 playerEyePos = player.getEyePosition(1.0F); // Posição dos olhos do jogador
					Vec3 bossPos = boss.position().add(0, boss.getBbHeight() / 2.0, 0); // Centro aproximado do boss

					// Verifica obstruções de blocos
					BlockHitResult blockHitResult = world.clip(new ClipContext(
							playerEyePos,
							bossPos,
							ClipContext.Block.COLLIDER,
							ClipContext.Fluid.NONE,
							player
					));

					// Verifica obstruções de entidades
					EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
							world,
							player,
							playerEyePos,
							bossPos,
							player.getBoundingBox().expandTowards(lookVec.scale(64)), // Alcance de 64 blocos
							e -> e == boss // Filtra apenas o boss como alvo válido
					);

					// Verifica se não há bloco ou entidade obstruindo
					if ((blockHitResult.getType() == HitResult.Type.MISS || blockHitResult.getBlockPos().equals(boss.blockPosition()))
							&& entityHitResult != null && entityHitResult.getEntity() == boss) {
						if (!player.getLevel().isClientSide()) {
							if (ProcessTransfur.isPlayerNotLatex(player)) {
								player.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2)); // Aplica "Radiação"
							} else {
								player.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 2)); // Aplica "Radiação"
							}
						}
						/*if (entity != null && boss != null){
							CameraUtil.tugEntityLookDirection(player, boss, 0.095);
							// player.hurt(DamageSource.mobAttack(boss).bypassInvul().bypassArmor(), 1.0F); // Causa dano fraco
						}*/
					}
				}
			}
		}

		public void tugPlayerLook(Player player, Vec3 target, double strength) {
			// Obtém a posição dos olhos do jogador
			Vec3 eyePosition = player.getEyePosition(1.0F);

			// Calcula a direção entre o jogador e o alvo
			Vec3 direction = target.subtract(eyePosition);

			// Calcula as rotações necessárias
			double distanceXZ = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
			float targetYaw = (float) (Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0F);
			float targetPitch = (float) -Math.toDegrees(Math.atan2(direction.y, distanceXZ));

			// Aplica rotação suavemente com força (interpolação)
			float newYaw = (float) (player.getYRot() + (targetYaw - player.getYRot()) * strength);
			float newPitch = (float) (player.getXRot() + (targetPitch - player.getXRot()) * strength);

			player.setYRot(newYaw);
			player.setXRot(newPitch);

			// Atualiza as rotações anteriores para suavizar
			player.yRotO = newYaw;
			player.xRotO = newPitch;
		}



		// Hypnotic Gaze Ability
		public void hypnoticGaze(LivingEntity entity) {
			LivingEntity self = boss;
			Level level = boss.getLevel();
			level.getNearbyEntities(Mob.class, TargetingConditions.DEFAULT, self, AABB.ofSize(self.position(), 3.0, 3.0, 3.0)).forEach((mob) -> {
				if (mob.getTarget() != null && mob.getTarget().is(self)) {
					mob.setTarget((LivingEntity)null);
				}
			});
			level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, self, AABB.ofSize(self.position(), 10.0, 10.0, 10.0)).forEach((livingEntity) -> {
				if (!(livingEntity.getLookAngle().dot(self.getEyePosition().subtract(livingEntity.getEyePosition()).normalize()) < 0.8500000238418579)) {
					if (!(livingEntity instanceof Player) || (Boolean) Changed.config.server.playerControllingAbilities.get()) {
						CameraUtil.tugEntityLookDirection(livingEntity, self, 0.125);
						if (!livingEntity.getLevel().isClientSide()) {
							livingEntity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 2, false, false), self);
							livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 2, false, false), self);
						}
					}
				}
			});
		}

		public void Passives(){
			if (boss.isActivatedAbility()) {
				for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(10.0))) {
					// Verifica a porcentagem de vida do alvo para determinar qual habilidade passiva usar
					double healthPercentage = (entity.getHealth() / entity.getMaxHealth()) * 100;
					boolean isToUseToxicGlare = healthPercentage > 15;
					//boolean isToUseHypnoticGaze = healthPercentage <= 15;

					// Executa as habilidades com base na porcentagem de vida do alvo
					if (isToUseToxicGlare) {
						toxicGlare(entity);
						this.boss.PassivesTicksCooldown ++;
					}
					// else if (isToUseHypnoticGaze) {
						//hypnoticGaze(entity);
					//}
				}
			}
		}

		// Irradiated Pulse Ability
		public void irradiatedPulse() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
				for (int i = 0; i < 5; i++) {
					PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.getLevel(), ParticleTypes.DRAGON_BREATH, this.boss.position(), 0.3f, 0.2f, 0.3f, 15,0);
				}
				for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(6.0))) {
					if (entity != boss) {
						if (!entity.getLevel().isClientSide()) {
							entity.addEffect(new MobEffectInstance(MobEffects.POISON, 120, 1)); // Aplica veneno por 6 segundos
						}
						entity.hurt(DamageSource.mobAttack(boss), 6.0F); // Dano direto
					}
				}
				boss.playSound(SoundEvents.BEACON_ACTIVATE, 4.5f, 0);
				boss.AbilitiesTicksCooldown = 100; // Set cooldown for the next activation
			}
		}

		// Radiant Field Ability
		public void radiantField() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
				// Partículas em círculo ao redor do boss
				/*
				 * for (int i = 0; i < 360; i += 15) {
					double angle = Math.toRadians(i);
					double x = boss.getX() + Mth.cos((float) angle) * 4.0;
					double z = boss.getZ() + Mth.sin((float) angle) * 4.0;
					Vec3 pos = new Vec3(x, boss.getY(), z);
					PlayerUtilProcedure.ParticlesUtil.sendParticles(
							boss.getLevel(),
							ParticleTypes.GLOW,
							pos,
							0.1f, 0.2f, 0.1f,
							15, 0
					);
				}*/

				
				for (int theta = 0; theta < 360; theta += 15) { // Ângulo horizontal
    				double angleTheta = Math.toRadians(theta);
    				for (int phi = 0; phi <= 180; phi += 15) { // Ângulo vertical
        				double anglePhi = Math.toRadians(phi);
        				double x = this.boss.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * 4.0;
        				double y = this.boss.getY() + Math.cos(anglePhi) * 4.0;
        				double z = this.boss.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * 4.0;
        				Vec3 pos = new Vec3(x, y, z);
        				PlayerUtilProcedure.ParticlesUtil.sendParticles(
                		this.boss.getLevel(),
                		ParticleTypes.GLOW,
                		pos,
                		0.3f, 0.2f, 0.3f,
				        15, 0
        				);
    				}
				}

				// Dano e efeitos em entidades próximas
				for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(4.0))) {
					if (entity != boss) {
						entity.hurt(DamageSource.mobAttack(boss), 2.0F); // Dano leve
						if (!entity.getLevel().isClientSide()) {
							entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0)); // Bloqueia regeneração
						}
					}
				}

				// Som e cooldown
				boss.playSound(SoundEvents.BEACON_DEACTIVATE, 4.5f, 1);
				boss.AbilitiesTicksCooldown = 100; // Set cooldown for the next activation
			}
		}


		// Meltdown Ability - Agora com partículas de esfera ao redor da Entity
		public void meltdown() {
			if (boss.isActivatedAbility() && boss.AbilitiesTicksCooldown <= 0) {
				PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.getLevel(), ParticleTypes.EXPLOSION, this.boss.position(), 0.3f, 0.2f, 0.3f, 15,0);

				// Para cada entidade dentro da área do meltdown
				for (LivingEntity entity : boss.level.getEntitiesOfClass(LivingEntity.class, boss.getBoundingBox().inflate(5.0))) {
					if (entity != boss) {
						entity.hurt(DamageSource.mobAttack(boss).bypassArmor(), 14.0F); // Dano alto
						if (!entity.getLevel().isClientSide()) {
							entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0)); // Aplica cegueira
						}

				for (int theta = 0; theta < 360; theta += 15) { // Ângulo horizontal
    				double angleTheta = Math.toRadians(theta);
    				for (int phi = 0; phi <= 180; phi += 15) { // Ângulo vertical
        				double anglePhi = Math.toRadians(phi);
        				double x = this.boss.getX() + Math.sin(anglePhi) * Math.cos(angleTheta) * 4.0;
        				double y = this.boss.getY() + Math.cos(anglePhi) * 4.0;
        				double z = this.boss.getZ() + Math.sin(anglePhi) * Math.sin(angleTheta) * 4.0;
        				Vec3 pos = new Vec3(x, y, z);
        				PlayerUtilProcedure.ParticlesUtil.sendParticles(
                		this.boss.getLevel(),
                		ParticleTypes.SMOKE,
                		pos,
                		0.3f, 0.2f, 0.3f,
				        15, 0
        				);
    				}
				}
						/*
						*for (int i = 0; i < 360; i += 15) {

							double angle = Math.toRadians(i);
							double x = entity.getX() + Math.cos(angle) * 2.0; // Distância esférica
							double z = entity.getZ() + Math.sin(angle) * 2.0; // Distância esférica
							double y = entity.getY() + Math.sin(angle) * 2.0; // Distância esférica em Y
							Vec3 pos = new Vec3(x,y,z);
							PlayerUtilProcedure.ParticlesUtil.sendParticles(this.boss.getLevel(), ParticleTypes.SMOKE, pos, 0.3f, 0, 0.3f, 3,0);
						}
						*/
					}
				}

				// O boss fica exausto
				boss.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1));
				boss.playSound(SoundEvents.BEACON_DEACTIVATE, 4.5f, 0);
				boss.AbilitiesTicksCooldown = 30; // Set cooldown for the next activation
				boss.PassivesTicksCooldown = 150;
			}
		}
	}
}
