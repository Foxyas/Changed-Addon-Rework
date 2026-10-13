package net.foxyas.changedaddon.process;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.foxyas.changedaddon.init.ChangedAddonMobEffects;
import net.foxyas.changedaddon.network.ChangedAddonVariables;
import net.foxyas.changedaddon.util.TagUtils;
import net.ltxprogrammer.changed.ability.IAbstractChangedEntity;
import net.ltxprogrammer.changed.process.ProcessTransfur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class UntransfurProgressInstance {

    public static final Codec<UntransfurProgressInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("untransfurProgress").forGetter(UntransfurProgressInstance::getUntransfurProgress),
            Codec.BOOL.fieldOf("shouldProgressDecay").forGetter(UntransfurProgressInstance::isShouldProgressDecay),
            Codec.BOOL.fieldOf("regressOnAssimilation").forGetter(UntransfurProgressInstance::isRegressOnAssimilation)
    ).apply(instance, UntransfurProgressInstance::new));

    public float untransfurProgress = 0f;
    public boolean shouldProgressDecay = true;
    public boolean regressOnAssimilation = true;

    public UntransfurProgressInstance() {}

    public UntransfurProgressInstance(float untransfurProgress, boolean shouldProgressDecay, boolean regressOnAssimilation) {
        this.untransfurProgress = untransfurProgress;
        this.shouldProgressDecay = shouldProgressDecay;
        this.regressOnAssimilation = regressOnAssimilation;
    }

    public float getUntransfurProgress() {
        return untransfurProgress;
    }

    public boolean isShouldProgressDecay() {
        return shouldProgressDecay;
    }

    public boolean isRegressOnAssimilation() {
        return regressOnAssimilation;
    }

    // ==========================================
    // Copy Method
    // ==========================================

    /**
     * Copies all values from another instance into this one.
     * Returns `this` to allow method chaining.
     */
    @SuppressWarnings("UnusedReturnValue")
    public UntransfurProgressInstance copyFromInstance(UntransfurProgressInstance other) {
        if (other != null) {
            this.untransfurProgress = other.untransfurProgress;
            this.shouldProgressDecay = other.shouldProgressDecay;
            this.regressOnAssimilation = other.regressOnAssimilation;
        }
        return this;
    }

    // ==========================================
    // Save and Load Methods
    // ==========================================

    /**
     * Saves this instance into a parent CompoundTag.
     */
    public void save(CompoundTag tag) {
        TagUtils.saveWithCodec(CODEC, this, tag, "untransfurProgress"); // Uses TagUtils codec encoding
    }

    /**
     * Loads data from a CompoundTag directly into this instance.
     */
    public UntransfurProgressInstance load(CompoundTag tag) {
        TagUtils.loadWithCodec(CODEC, tag, "untransfurProgress") // Uses TagUtils strict decoding
                .ifPresent(this::copyFromInstance);
        return this;
    }

    public void regressTransfurProgress(Player player, float value) {
        this.untransfurProgress -= value;
    }

    public void progressTransfurProgress(Player player, float value) {
        this.untransfurProgress += value;
    }

    public void resetUntransfurProgress(Player player) {
        this.untransfurProgress = 0;
    }

    public void onPlayerAbsorbEntity(LivingEntity target, IAbstractChangedEntity source) {
        if (source.getEntity() instanceof Player player) {
            regressTransfurProgress(player, 0.95f);
        }
    }

    public void onPlayerAssimilateEntity(LivingEntity target, IAbstractChangedEntity source) {
        if (source.getEntity() instanceof Player player) {
            regressTransfurProgress(player, 0.7f);
        }
    }

    public void tick(Player player) {
        ChangedAddonVariables.PlayerVariables vars = ChangedAddonVariables.of(player);
        if (vars == null) return;

        boolean hasEffect = player.hasEffect(ChangedAddonMobEffects.UNTRANSFUR.get());
        boolean isTransfurred = ProcessTransfur.isPlayerTransfurred(player);

        // Case 1: Player doesn't have the effect, but still has progress -> Decay it
        float untransfurProgress = getUntransfurProgress();

        if (!hasEffect) {
            if (untransfurProgress > 0) {
                regressTransfurProgress(player, 0.001f);
                vars.syncPlayerVariables(player);
            }
            return;
        }

        // Case 2: Has effect, but isn't transfurred -> Nothing to untransfur
        if (!isTransfurred) return;

        // Case 3: Progress is somehow negative -> Reset it
        if (untransfurProgress < 0) {
            resetUntransfurProgress(player);
            vars.syncPlayerVariables(player); // Added missing sync for reset
        } else {
            // Case 4: Standard progress progression
            float defaultValue = ProcessTransfur.isPlayerNotLatex(player) ? 0.001f : 0.002f;

            float sleepMultiplier = 1.0f;
            if (player.isSleeping()) {
                sleepMultiplier = player.isSleepingLongEnough() ? 2.5f : 1.25f;
            }

            float progressValue = defaultValue * sleepMultiplier;
            progressTransfurProgress(player, progressValue);
        }

        vars.syncPlayerVariables(player);
    }
}