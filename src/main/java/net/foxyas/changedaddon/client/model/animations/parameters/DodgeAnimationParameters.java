package net.foxyas.changedaddon.client.model.animations.parameters;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.ltxprogrammer.changed.entity.animation.AnimationAssociation;
import net.ltxprogrammer.changed.entity.animation.AnimationParameters;
import net.minecraft.world.entity.LivingEntity;

public record DodgeAnimationParameters(float speed, float maxTimeSpam) implements AnimationParameters {
    public static DodgeAnimationParameters DEFAULT = new DodgeAnimationParameters(1, 1.5f);
    public static DodgeAnimationParameters FAST = new DodgeAnimationParameters(2, 1.0f);
    public static DodgeAnimationParameters SLOW = new DodgeAnimationParameters(0.5f, 3);

    public static final Codec<DodgeAnimationParameters> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("speed").forGetter(DodgeAnimationParameters::speed),
                    Codec.FLOAT.fieldOf("maxTimeSpam").forGetter(DodgeAnimationParameters::maxTimeSpam)
            ).apply(instance, DodgeAnimationParameters::new)
    );

    @Override
    public AnimationAssociation.Match matchesAssociation(AnimationAssociation association) {
        return AnimationAssociation.Match.DEFAULT;
    }

    @Override
    public boolean shouldEndAnimation(LivingEntity livingEntity, float totalTime) {
        // "Stop when entity is hurt"
        if (livingEntity.hurtTime > 0) {
            return true;
        }

        return totalTime > maxTimeSpam;
    }

    @Override
    public boolean shouldLoop(LivingEntity livingEntity, float totalTime) {
        return false;
    }

    @Override
    public float getPlaybackSpeed(LivingEntity livingEntity, float totalTime) {
        return speed;
    }
}