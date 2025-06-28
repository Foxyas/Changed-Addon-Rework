package net.foxyas.changedaddon.entity;

import net.foxyas.changedaddon.init.ChangedAddonModEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.init.ChangedAttributes;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

public class ReynEntity extends ChangedEntity {

    //private static final String FORCE_GLOW_TAG = "ForceGlowDisplay";
    //private static final String GLOW_TAG = "GlowDisplay";

    //public boolean ForceGlowDisplay = false;
    //public boolean GlowDisplay;
    public ReynEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ChangedAddonModEntities.REYN.get(), world);
    }

    public ReynEntity(EntityType<ReynEntity> type, Level world) {
        super(type, world);

        xpReward = 5;
        this.setAttributes(this.getAttributes());
        setNoAi(false);
    }

    public static void init() {
    }
	/*
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains(FORCE_GLOW_TAG)){
			ForceGlowDisplay = tag.getBoolean(FORCE_GLOW_TAG);
		}
		if (tag.contains(GLOW_TAG)){
			GlowDisplay = tag.getBoolean(GLOW_TAG);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean(FORCE_GLOW_TAG,ForceGlowDisplay);
		tag.putBoolean(GLOW_TAG,GlowDisplay);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if (this.GlowDisplay != ShouldGlow()){
			this.GlowDisplay = ShouldGlow();
		}
	}

	public boolean ShouldGlow(){
		// Maybe can be optimized but ok
		if (!ForceGlowDisplay && isDarkAroundEntity()){
			return true;
		} else if (ForceGlowDisplay && !isDarkAroundEntity()) {
			return true;
		} else if (ForceGlowDisplay && isDarkAroundEntity()) {
			return true;
		}

		return isDarkAroundEntity();
	}

	private boolean isDarkAroundEntity() {
		// Pega a posição atual da entidade
		BlockPos entityPos = this.blockPosition();

		// Nível de luz no bloco (inclui luz de blocos e luz do céu)
		Level level = this.level();

		// Obtém a luz do bloco (luz artificial, como tochas)
		int blockLight = level.getBrightness(LightLayer.BLOCK, entityPos);

		// Obtém a luz do céu (luz natural, como sol e lua)
		int skyLight = level.getBrightness(LightLayer.SKY, entityPos);

		// Combina ambos para verificar o nível de luz total (artificial + natural)
		int totalLightLevel = Math.max(blockLight, skyLight);

		// Considera escuro se o nível de luz total for menor ou igual a 9 (ponto de spawn de mobs)
		return skyLight <= 9;
	}
*/

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
        Objects.requireNonNull(attributes.getInstance(ChangedAttributes.TRANSFUR_DAMAGE.get())).setBaseValue((0));
        attributes.getInstance(Attributes.MAX_HEALTH).setBaseValue((24));
        attributes.getInstance(Attributes.FOLLOW_RANGE).setBaseValue(40.0f);
        attributes.getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(1.05f);
        attributes.getInstance(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.95f);
        attributes.getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(3.0f);
        attributes.getInstance(Attributes.ARMOR).setBaseValue(0);
        attributes.getInstance(Attributes.ARMOR_TOUGHNESS).setBaseValue(0);
        attributes.getInstance(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    @Override
    public Color3 getHairColor(int i) {
        return Color3.getColor("##4c4c4c");
    }

    @Override
    public LatexType getLatexType() {
        return LatexType.NEUTRAL;
    }

    @Override
    public TransfurMode getTransfurMode() {
        return TransfurMode.NONE;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        HairStyle Hair = BALD.get();
        if (level().random.nextInt(10) > 5) {
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
        if (level().random.nextInt(10) > 5) {
            color = Color3.getColor("#4c4c4c");
        } else {
            color = Color3.getColor("#5c5c5c");
        }
        return color;
    }

    public Color3 getTransfurColor(TransfurCause cause) {
        return Color3.getColor("#4c4c4c");
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
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
        return super.getMyRidingOffset();
    }

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.hurt"));
    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.death"));
    }
}
