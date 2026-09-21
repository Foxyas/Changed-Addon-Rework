package net.foxyas.changedaddon.process;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.entity.api.LivingEntityDataExtensor;
import net.foxyas.changedaddon.init.ChangedAddonTags;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.ParticlesUtil;
import net.foxyas.changedaddon.util.PlayerUtil;
import net.foxyas.changedaddon.variant.TransfurSoundsDetails;
import net.ltxprogrammer.changed.entity.ChangedEntity;
import net.ltxprogrammer.changed.entity.Emote;
import net.ltxprogrammer.changed.init.ChangedParticles;
import net.ltxprogrammer.changed.util.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class CuddleHandle {

    public static final Codec<CuddleHandle> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("cuddling").forGetter(CuddleHandle::wantToCuddles),
            Codec.INT.fieldOf("ticksCuddling").forGetter(CuddleHandle::getTicksCuddling)
    ).apply(instance, (cuddling, ticks) -> {
        CuddleHandle handle = new CuddleHandle();
        handle.wantToCuddle = cuddling;
        handle.ticksCuddling = ticks;
        return handle;
    }));


    public boolean wantToCuddle = false;
    public int ticksCuddling = 0;

    public CuddleHandle() {
        super();
    }

    public void save(CompoundTag tag) {
        CODEC.encodeStart(NbtOps.INSTANCE, this)
                .resultOrPartial(errorMessage -> System.err.println("Failed to encode CuddleHandle: " + errorMessage))
                .ifPresent(encodedTag -> {
                    if (encodedTag instanceof CompoundTag compound) {
                        tag.put("cuddleHandle", compound);
                    }
                });
    }

    public void load(CompoundTag tag) {
        if (!tag.contains("cuddleHandle")) return;

        CODEC.parse(NbtOps.INSTANCE, tag.getCompound("cuddleHandle"))
                .resultOrPartial(errorMessage -> System.err.println("Failed to parse CuddleHandle: " + errorMessage))
                .ifPresent(parsedInstance -> {
                    this.wantToCuddle = parsedInstance.wantToCuddle;
                    this.ticksCuddling = parsedInstance.ticksCuddling;
                });
    }

    public void tick(Player player) {
        if (!wantToCuddles()) {
            stopCuddling();
            return;
        }

        // Validate state only after checking resolution
        if (!validateCuddleState(player)) {
            this.stopCuddling();
        } else {
            // Only increment ticks if valid cuddle
            this.ticksCuddling++;

            mayForcePlayerToNeverSleepEnough(player);
        }
    }


    public void mayForcePlayerToNeverSleepEnough(Player sleeper) {
        if (!sleeper.isSleeping()) return;

        ChangedAddonVariables.PlayerVariables playerVariables = ChangedAddonVariables.ofOrDefault(sleeper);
        if (!playerVariables.wantToCuddles()) return;
        if (!PlayerUtil.isCuddleStateValidForBed(sleeper)) {
            playerVariables.setWantCuddles(false);
            playerVariables.syncPlayerVariables(sleeper);
            sleeper.displayClientMessage(Component.translatable("text.changed_addon.invalid_cuddle_state"), true);
            return;
        } else {
            LivingEntity cuddler = PlayerUtil.getCuddlerFrom(sleeper);
            if (cuddler != null && ticksCuddling % 260 == 0) {
                if (EntityUtil.maybeGetOverlaying(cuddler) instanceof ChangedEntity changedEntity) {
                    boolean catLike = changedEntity.getSelfVariant().is(ChangedAddonTags.TransfurVariants.CAT_LIKE);
                    boolean wolfLike = changedEntity.getSelfVariant().is(ChangedAddonTags.TransfurVariants.CAT_LIKE);
                    if (catLike) {
                        cuddler.playSound(SoundEvents.CAT_PURR);
                    }
                    if (wolfLike) {
                        cuddler.playSound(SoundEvents.WOLF_WHINE);
                    }
                }
                ParticlesUtil.sendParticles(cuddler.level(),
                        ChangedParticles.emote(cuddler, Emote.HEART),
                        new Vec3(
                                cuddler.getX(),
                                cuddler.getY() + (double) cuddler.getDimensions(cuddler.getPose()).height + 0.65,
                                cuddler.getZ()
                        ),
                        Vec3.ZERO,
                        0,
                        1
                );
                sleeper.heal(0.05f);
                cuddler.heal(0.05f);
            }
        }

        LivingEntityDataExtensor ext = LivingEntityDataExtensor.ofEntity(sleeper);
        if (ext == null) return;

        ext.setSleepCounter(1);
    }

    public boolean validateCuddleState(Player player) {
        LivingEntity cuddlerFrom = PlayerUtil.getCuddlerFrom(player);
        LivingEntity cuddledFrom = PlayerUtil.getCuddledFrom(player);
        if (cuddlerFrom == null && cuddledFrom == null) {
            return false;
        }
        LivingEntity cuddlingWith = Objects.requireNonNullElse(cuddlerFrom, cuddledFrom);


        return !cuddlingWith.isDeadOrDying() && !cuddlingWith.isRemoved();
    }

    public void stopCuddling() {
        this.wantToCuddle = false;
        this.ticksCuddling = 0;
    }

    public int getTicksCuddling() {
        return ticksCuddling;
    }

    public void setTicksCuddling(int ticksCuddling) {
        this.ticksCuddling = ticksCuddling;
    }

    public void setWantToCuddle(boolean cuddleState) {
        this.wantToCuddle = cuddleState;
    }

    public boolean wantToCuddles() {
        return wantToCuddle;
    }
}