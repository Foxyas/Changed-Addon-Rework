package net.foxyas.changedaddon.entity.simple;

import net.foxyas.changedaddon.init.ChangedAddonEntities;
import net.ltxprogrammer.changed.entity.*;
import net.ltxprogrammer.changed.util.Color3;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

import static net.ltxprogrammer.changed.entity.HairStyle.BALD;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LatexSnowFoxMaleEntity extends AbstractSnowFoxEntity {

    public LatexSnowFoxMaleEntity(PlayMessages.SpawnEntity ignoredPacket, Level world) {
        this(ChangedAddonEntities.LATEX_SNOW_FOX_MALE.get(), world);
    }

    public LatexSnowFoxMaleEntity(EntityType<LatexSnowFoxMaleEntity> type, Level world) {
        super(type, world);
        xpReward = 5;
        this.setAttributes(this.getAttributes());
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void init(SpawnPlacementRegisterEvent event) {
        event.register(ChangedAddonEntities.LATEX_SNOW_FOX_MALE.get(),
                SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ChangedEntity::checkEntitySpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    public Color3 getHairColor(int i) {
        return Color3.getColor("#E5E5E5");
    }

    @Override
    public int getTicksRequiredToFreeze() {
        return 700;
    }


    @Override
    public TransfurMode getTransfurMode() {
        if (random.nextInt(10) > 5) {
            return TransfurMode.ABSORPTION;
        }
        return TransfurMode.REPLICATION;
    }

    @Override
    public HairStyle getDefaultHairStyle() {
        HairStyle Hair;
        if (random.nextInt(10) > 5) {
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
        Color3 color;
        if (random.nextInt(10) > 5) {
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
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
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
    public @NotNull MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GENERIC_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GENERIC_DEATH;
    }
}