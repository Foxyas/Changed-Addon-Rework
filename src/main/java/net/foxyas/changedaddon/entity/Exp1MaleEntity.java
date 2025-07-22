package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
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

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

@Mod.EventBusSubscriber
public class Exp1MaleEntity extends ChangedEntity implements GenderedEntity, PowderSnowWalkable {
    private static final Set<ResourceLocation> SPAWN_BIOMES = Set.of(new ResourceLocation("snowy_plains"), new ResourceLocation("snowy_taiga"), new ResourceLocation("snowy_beach"));

    public Exp1MaleEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonEntities.EXP_1_MALE.get(), world);
    }

    public Exp1MaleEntity(EntityType<Exp1MaleEntity> type, Level world) {
        super(type, world);
        maxUpStep = 0.6f;
        xpReward = 5;
        this.setAttributes(this.getAttributes());
        setNoAi(false);
    }

    @SubscribeEvent
    public static void addLivingEntityToBiomes(BiomeLoadingEvent event) {
        if (SPAWN_BIOMES.contains(event.getName()))
            event.getSpawns().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(ChangedAddonEntities.EXP_1_MALE.get(), 20, 1, 4));
    }

    public static void init() {
        SpawnPlacements.register(ChangedAddonEntities.EXP_1_MALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)));
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(ChangedAttributes.TRANSFUR_DAMAGE.get(), 0);
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 24);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    protected void setAttributes(AttributeMap attributes) {
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((3));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.1f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.95f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    @Override
    public Color3 getHairColor(int i) {
        return Color3.getColor("#E5E5E5");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 700;
    }

    @Override
    public LatexType getLatexType() {
        return LatexType.NEUTRAL;
    }

    @Override
    public TransfurMode getTransfurMode() {
        if (level.random.nextInt(10) > 5) {
            return TransfurMode.ABSORPTION;
        }
        return TransfurMode.REPLICATION;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        HairStyle Hair = BALD.get();
        if (level.random.nextInt(10) > 5) {
            Hair = HairStyle.SHORT_MESSY.get();
        } else {
            Hair = BALD.get();
        }
        return Hair;
    }

    @Override
    public @Nullable List<HairStyle> getValidHairStyles() {
        return HairStyle.Collection.MALE.getStyles();
    }

    public Color3 getDripColor() {
        Color3 color = Color3.getColor("#ffffff");
        if (level.random.nextInt(10) > 5) {
            color = Color3.getColor("#ffffff");
        } else {
            color = Color3.getColor("#e0e0e0");
        }
        return color;
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#e0e0e0");
    }

    @Override
    public Gender getGender() {
        return Gender.MALE;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
		/*
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1));
		this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(5, new FloatGoal(this));
		*/
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
    }
}