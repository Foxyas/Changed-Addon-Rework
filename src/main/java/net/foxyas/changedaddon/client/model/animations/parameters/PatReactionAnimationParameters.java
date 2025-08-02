package net.foxyas.changedaddon.client.model.animations.parameters;

import com.mojang.serialization.Codec;
import net.ltxprogrammer.changed.entity.animation.AnimationAssociation;
import net.ltxprogrammer.changed.entity.animation.AnimationParameters;
import net.minecraft.world.entity.LivingEntity;

public class PatReactionAnimationParameters implements AnimationParameters {
    public static PatReactionAnimationParameters INSTANCE = new PatReactionAnimationParameters();

    public static Codec<PatReactionAnimationParameters> CODEC = Codec.unit(() -> INSTANCE);

    private PatReactionAnimationParameters() {
    }

    @Override
    public AnimationAssociation.Match matchesAssociation(AnimationAssociation association) {
        return AnimationAssociation.Match.DEFAULT;
    }

    @Override
    public boolean shouldEndAnimation(LivingEntity livingEntity, float totalTime) {
        return totalTime > 0.5f;
    }

    @Override
    public boolean shouldLoop(LivingEntity livingEntity, float totalTime) {
        return false;
    }
}