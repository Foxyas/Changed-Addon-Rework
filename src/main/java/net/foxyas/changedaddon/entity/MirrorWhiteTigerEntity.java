
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Mod.EventBusSubscriber
public class MirrorWhiteTigerEntity extends ChangedEntity implements PowderSnowWalkable,GenderedEntity {

	private static final Set<ResourceLocation> SPAWN_BIOMES = Set.of(new ResourceLocation("taiga")/*, new ResourceLocation("icy") */);
	@SubscribeEvent
	public static void addLivingEntityToBiomes(BiomeLoadingEvent event) {
		if (SPAWN_BIOMES.contains(event.getName()))
			event.getSpawns().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ChangedAddonModEntities.MIRROR_WHITE_TIGER.get(), 20, 1, 4));
	}

	public MirrorWhiteTigerEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.MIRROR_WHITE_TIGER.get(), world);
	}

	public MirrorWhiteTigerEntity(EntityType<MirrorWhiteTigerEntity> type, Level world) {
		super(type, world);
		//this.setAttributes(getAttributes());
		maxUpStep = 0.6f;
		xpReward = ChangedEntity.XP_REWARD_MEDIUM;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
	}

	protected void setAttributes(AttributeMap attributes) {
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((0));
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.15f);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(0.95f);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(4.0);
		//attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		//attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}

	public boolean causeFallDamage(float p_148859_, float p_148860_, DamageSource p_148861_) {
		return false;
	}
	@Override
	public Color3 getDripColor() {
		return Color3.WHITE;
	}

	public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.GRAY;
    }

	@Override
	public int getTicksRequiredToFreeze() {
		return 420;
	}
	@Override
	public HairStyle getDefaultHairStyle() {
		return HairStyle.BALD.get();
	}

	@Override
	public LatexType getLatexType() {
		return LatexType.NEUTRAL;
	}

	public @Nullable List<HairStyle> getValidHairStyles() {
		return HairStyle.Collection.MALE.getStyles();
	}

	@Override
	public TransfurMode getTransfurMode() {
		return TransfurMode.REPLICATION;
	}

	@Override
	public Color3 getHairColor(int layer) {
		return Color3.WHITE;
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
	public double getMyRidingOffset() {
		return -0.35D;
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
		SpawnPlacements.register(ChangedAddonModEntities.MIRROR_WHITE_TIGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				(entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 4);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}

	@Override
	public Gender getGender() {
		return Gender.FEMALE;
	}
}
