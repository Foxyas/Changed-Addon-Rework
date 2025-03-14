
package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.ChangedAddonMod;
import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.foxyas.changedaddon.init.ChangedAddonModGameRules;
import net.foxyas.changedaddon.init.ChangedAddonModBlocks;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;
import java.util.Set;

@Mod.EventBusSubscriber
public class DazedEntity extends ChangedEntity {

	// Definindo a chave de sincronização no seu código
	private static final EntityDataAccessor<Boolean> DATA_PUDDLE_ID = SynchedEntityData.defineId(DazedEntity.class, EntityDataSerializers.BOOLEAN);
	public boolean Morphed = false;
	public static UseItemMode PuddleForm = UseItemMode.create("PuddleForm",false,false,false,true,false);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_PUDDLE_ID, false); // Define o valor inicial como 'false'
	}

	// Getter para checar se está no estado morphed
	public boolean isMorphed() {
		return this.entityData.get(DATA_PUDDLE_ID);
	}

	public boolean isMorphed(boolean nbt) {
		return !nbt ? this.entityData.get(DATA_PUDDLE_ID) : isMorphed();
	}

	// Setter para alterar o estado morphed
	public void setMorphed(boolean morphed) {
		this.entityData.set(DATA_PUDDLE_ID, morphed);
	}

	public void setMorphed(boolean morphed, boolean nbt) {
		if (!nbt){
			this.entityData.set(DATA_PUDDLE_ID, morphed);
		} else {
			this.Morphed = morphed;
		}
	}

	
	private static final Set<ResourceLocation> SPAWN_BIOMES = Set.of(new ResourceLocation("plains"));

	@SubscribeEvent
	public static void addLivingEntityToBiomes(BiomeLoadingEvent event) {
		if (SPAWN_BIOMES.contains(event.getName())){
			event.getSpawns().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ChangedAddonModEntities.DAZED.get(), 125, 1, 4));
		}
	}

	public DazedEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(ChangedAddonModEntities.DAZED.get(), world);
	}

	public DazedEntity(EntityType<DazedEntity> type, Level world) {
		super(type, world);
		maxUpStep = 0.6f;
		xpReward = 0;
		this.setAttributes(this.getAttributes());
		setNoAi(false);
		setPersistenceRequired();
	}

	protected void setAttributes(AttributeMap attributes) {
		Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
		attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((26));
		attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
		attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.075F);
		attributes.getInstance((Attribute) ForgeMod.SWIM_SPEED.get()).setBaseValue(1.025F);
		attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
		attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
		attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
		attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Morphed")){
			this.Morphed = tag.getBoolean("Morphed");
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("Morphed",Morphed);
	}

	@Override
	public float getEyeHeightMul() {
		if (this.isMorphed())
			return 0.4F;
		else
			return super.getEyeHeightMul();
	}
	

	@Override
	public EntityDimensions getDimensions(Pose pose) {
		EntityDimensions core = super.getDimensions(pose);
		if (this.isMorphed())
			return EntityDimensions.scalable(core.width - 0.05f, core.height - 1.25f);
		else
			return core;
	}

	@Override
	public UseItemMode getItemUseMode() {
		if (this.isMorphed()){
			return this.PuddleForm;
		}
		return super.getItemUseMode();
	}

	@Override
	public Color3 getHairColor(int i) {
		return Color3.getColor("#E5E5E5");
	}

	@Override
	public int getTicksRequiredToFreeze() { return 700; }

	@Override
	public LatexType getLatexType() {
		return LatexType.NEUTRAL;
	}

	@Override
	public TransfurMode getTransfurMode() {
		TransfurMode transfurMode = TransfurMode.REPLICATION;
		if(level.random.nextInt(10) > 5){ transfurMode = TransfurMode.ABSORPTION;
		} else {
			transfurMode = TransfurMode.REPLICATION;
		}
		return transfurMode;
	}

	@Override
	public HairStyle getDefaultHairStyle() {
		HairStyle Hair = BALD.get();
		if(level.random.nextInt(10) > 5){ Hair = HairStyle.SHORT_MESSY.get();
		} else {
			Hair = BALD.get();
		}
		return Hair;
	}

	@Override
	public @Nullable List<HairStyle> getValidHairStyles() {
		return HairStyle.Collection.MALE.getStyles();
	}

	@Override
	public Color3 getDripColor() {
		Color3 color = Color3.getColor("#ffffff");
		if(level.random.nextInt(10) > 5){ color = Color3.getColor("#ffffff");;
		} else {
			color = Color3.getColor("#CFCFCF");
		}
		return color;
	}

	public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#CFCFCF");
    }

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new RestrictSunGoal(this) {
			@Override
			public boolean canUse() {
				double x = DazedEntity.this.getX();
				double y = DazedEntity.this.getY();
				double z = DazedEntity.this.getZ();
				Entity entity = DazedEntity.this;
				Level world = DazedEntity.this.level;
				return super.canUse() && world.getGameRules().getBoolean(ChangedAddonModGameRules.DO_DAZED_LATEX_BURN);
			}
		});

		/*this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new FloatGoal(this));*/
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
		SpawnPlacements.register(
				ChangedAddonModEntities.DAZED.get(),
				SpawnPlacements.Type.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				DazedEntity::canSpawnNear
		);
	}

	private static boolean canSpawnNear(EntityType<DazedEntity> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, Random random) {
		if (world.getDifficulty() == Difficulty.PEACEFUL) {
			return false;
		}

		if (!Monster.isDarkEnoughToSpawn(world, pos, random)) {
			//ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n isn't dark enough");
			return false;
		}

		if (!world.getBiome(pos).is(Tags.Biomes.IS_PLAINS)) {
			//ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n isn't plains");
			return false;
		}

		// Certifica-se de que o bloco abaixo não é ar e é sólido
		BlockState blockBelow = world.getBlockState(pos.below());
		if (!blockBelow.isSolidRender(world, pos.below()) || !blockBelow.isFaceSturdy(world, pos.below(), Direction.UP)) {
			//ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n isn't a good block");
			return false;
		}

		// Defina uma AABB (Área de Checagem) ao redor do spawn para verificar se há Oak Log por perto.
		AABB checkArea = new AABB(pos).inflate(32); // Raio de 32 blocos ao redor

		boolean nearSpawnBlock = world.getBlockStatesIfLoaded(checkArea)
				.anyMatch(state -> state.is(ChangedAddonModBlocks.GOO_CORE.get()));
		//ChangedAddonMod.LOGGER.info("A Try To Spawn A Dazed Entity in " + pos + "\n" + nearSpawnBlock);

		return nearSpawnBlock;
	}


	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder.add((Attribute) ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
		builder = builder.add(Attributes.MAX_HEALTH, 24);
		builder = builder.add(Attributes.ARMOR, 0);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
		return builder;
	}
}